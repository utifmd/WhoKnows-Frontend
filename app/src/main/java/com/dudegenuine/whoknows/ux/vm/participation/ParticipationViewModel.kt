package com.dudegenuine.whoknows.ux.vm.participation

import android.content.BroadcastReceiver
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import com.dudegenuine.model.*
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IParticipantUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IRoomUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUserUseCaseModule
import com.dudegenuine.whoknows.ux.compose.navigation.Screen
import com.dudegenuine.whoknows.ux.compose.state.*
import com.dudegenuine.whoknows.ux.compose.state.ResourceState.Companion.DONT_EMPTY
import com.dudegenuine.whoknows.ux.vm.BaseViewModel
import com.dudegenuine.whoknows.ux.vm.participation.contract.IParticipantViewModel
import com.dudegenuine.whoknows.ux.vm.room.contract.IRoomViewModel.Companion.KEY_PARTICIPATION_ROOM_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class ParticipationViewModel
    @Inject constructor(
    private val caseParticipation: IParticipantUseCaseModule,
    private val caseRoom: IRoomUseCaseModule,
    private val caseUser: IUserUseCaseModule,
    private val savedStateHandle: SavedStateHandle): BaseViewModel(), IParticipantViewModel {
    private val TAG: String = javaClass.simpleName

    private val prefs get() = caseParticipation.prefs
    val receiver get() = caseRoom.receiver

    /*private val _isParticipated = mutableStateOf(prefs.participantTimeLeft)
    val isParticipated get() = _isParticipated.value*/

    private val _timer = mutableStateOf(0.0)
    val timer get() = _timer.value

    /*private fun onParticipationCancel(){
        Log.d(TAG, "onParticipationCancel: ")
        _isParticipated.value = 0
        prefs.participantTimeLeft = isParticipated
    }*/

    init { onDecideParticipationRule() }

    private val savedRoomId = savedStateHandle.get<String>(KEY_PARTICIPATION_ROOM_ID)

    private fun onDecideParticipationRule(){
        /*val roomId = savedStateHandle.get<String>(KEY_PARTICIPATION_ROOM_ID) ?: return
        Log.d(TAG, "onParticipationRouted: $roomId")*/

        caseRoom.getBoarding()
            .onEach{ onResourceParticipation(it,
                ::onParticipationSucceed,
                ::onParticipationError) }
            .launchIn(viewModelScope)
    }
    private fun onParticipationSucceed(participation: Participation){
        onTimerChange(prefs.participationTimeLeft.toDouble())

        if (prefs.participationTimeLeft >= 0) {
            onParticipationChange(ResourceState.Boarding(data = participation))

            /*if(participation.roomIsParticipated)
                caseRoom.timer.start(participation.roomMinute.toDouble())*/
        }
        else onPreResult(participation)
    }
    private fun onParticipationError(message: String){
        Log.d(TAG, "onParticipationError: $message")
        savedRoomId?.let{ roomId -> getRoom(roomId){ room ->
            val isRoomMeetOldUser = room.participants.any{ prefs.userId == it.userId && !it.expired }

            if (isRoomMeetOldUser) onUpdateAndParticipant(room)
            else onCreateAnParticipant(room)
        }}
            ?: onParticipationChange(ResourceState.Boarding(error = "Can\'t recognize the class"))
    }
    private fun onCreateAnParticipant(room: Room.Complete) {
        Log.d(TAG, "onCreateAnParticipant: ${room.id}")
        val participant = ParticipantState().copy(
            roomId = room.id, userId = prefs.userId, timeLeft = room.minute)
        onBoarding(room, participant)
        postParticipant(participant)
    }
    private fun onUpdateAndParticipant(room: Room.Complete) {
        Log.d(TAG, "onUpdateAndParticipant: ${room.id}")
        getParticipant(prefs.userId, room.id){ participant ->
            onBoarding(room, participant)
        }
    }
    private fun onBoarding(room: Room.Complete, participant: Participant){
        getCurrentUser{ user ->
            Log.d(TAG, "onBoarding -> getCurrentUser: triggered")
            val participation = caseParticipation.getParticipation(participant.id, room, user)
            val taskDuration = (room.minute.toFloat() * 60).toDouble()

            onParticipationChange(ResourceState.Boarding(data = participation))
            caseRoom.timer.start(taskDuration)

            postBoarding(participation)
        }
    }
    override fun onPreResult(participation: Participation) {
        val questioners = participation.pages
        val correct = questioners.filter{ it.isCorrect }.map{ it.quiz.question }
        val wrong = questioners.filter{ !it.isCorrect }.map{ it.quiz.question }
        val correctSize = questioners.count{ it.isCorrect }
        val score = correctSize.toFloat() / questioners.size.toFloat() * 100
        val event = "${participation.user.username} has joined the ${participation.roomTitle}" //val result = event.plus(" with score ${resultScore.toDouble()}")
        val participant = ParticipantState().copy(
            id = participation.participantId,
            roomId = participation.roomId,
            userId = prefs.userId,
            currentPage = "${participation.pages.size}",
            timeLeft = timer.toInt()
        )
        val result = ResultState().copy(
            roomId = participation.roomId,
            userId = prefs.userId,
            correctQuiz = correct,
            wrongQuiz = wrong,
            score = score.toInt(),
        )
        val notification = NotificationState().copy(
            userId = prefs.userId,
            roomId = participation.roomId,
            event = event,
            recipientId = participation.userId
        )
        val addMessaging = Messaging.GroupAdder(
            keyName = participation.roomId,
            tokens = listOf(prefs.tokenId)
        )
        val pushMessaging = Messaging.Pusher(
            title = participation.roomTitle,
            body = event,
            largeIcon = participation.user.profileUrl
        )
        caseParticipation.postParticipation(participant, result, notification, addMessaging, pushMessaging)
            .onEach(::onResourceStateless)
            .onCompletion{ if (it == null)
                onParticipationDone(participant.roomId, participant.userId) }
            .launchIn(viewModelScope)
    }
    override fun onTimerReceiver(participation: Participation): BroadcastReceiver =
        receiver.timerReceiver { time, finished ->
            onTimerChange(time)
            if (finished) onPreResult(participation)
        }
    override fun patchBoarding(state: Participation) {
        if (state.pages.isEmpty() or state.roomId.isEmpty() or
            state.userId.isEmpty() or state.participantId.isEmpty()) {
            onToast(DONT_EMPTY)
            return
        }
        caseRoom.patchBoarding(state)
            .onEach(::onResourceStateless)
            .launchIn(viewModelScope)
    }
    override fun postBoarding(state: Participation) {
        if (state.pages.isEmpty() or state.roomId.isEmpty() or
            state.userId.isEmpty() or state.participantId.isEmpty()) {
            onToast(DONT_EMPTY)
            return
        }
        caseRoom.postBoarding(state)
            .onEach(::onResourceStateless)
            .launchIn(viewModelScope)
    }
    override fun onBoardingActionPressed(index: Int, type: Quiz.Action.Type ) { TODO("Not yet implemented") }
    override fun onBoardingBackPressed() = onScreenStateChange(ScreenState.Navigate.Back())
    override fun onBoardingPrevPressed() {
        val participation = participation.data ?: return
        val fresh = participation.apply{ currentQuestionIdx -= 1 }
        onParticipationChange(ResourceState.Boarding(data = fresh))
    }
    override fun onBoardingNextPressed() {
        val participation = participation.data ?: return
        val fresh = participation.apply{ currentQuestionIdx += 1 }
        onParticipationChange(ResourceState.Boarding(data = fresh))
    }
    override fun onBoardingDonePressed() {
        val participation = participation.data ?: return
        onPreResult(participation)
    }
    override fun onResultDismissPressed() {
        val route = Screen.Home.Summary.route
        val option = NavOptions.Builder().setPopUpTo(route, true).build()
        onScreenStateChange(ScreenState.Navigate.To(route, option))
    }
    private fun onParticipationDone(roomId: String, userId: String){
        val checkRoutePoint = Screen.Home.Summary.Participation.routeWithArgs("{$KEY_PARTICIPATION_ROOM_ID}")
        val option = NavOptions.Builder().setPopUpTo(checkRoutePoint, true).build()

        getFreshUser{ fresh ->
            onAuthChange(ResourceState.Auth(user = fresh))
            onParticipationChange(ResourceState.Boarding(data = null))
            onScreenStateChange(ScreenState.Navigate.To(Screen.Home.Summary.RoomDetail.ResultDetail.routeWithArgs(roomId, userId), option))
        }
    }
    override fun getRoom(roomId: String, onSucceed: (Room.Complete) -> Unit) {
        Log.d(TAG, "getRoom: $roomId")
        caseRoom.getRoom(roomId)
            .onEach{ onResourceStateless(it, onSucceed) }
            .launchIn(viewModelScope)
    }
    override fun getCurrentUser(onSucceed: (User.Complete) -> Unit) {
        caseUser.getUser()
            .onEach{ onResourceStateless(it, onSucceed) }
            .launchIn(viewModelScope)
    }
    private fun getFreshUser(onSucceed: (User.Complete) -> Unit) {
        caseUser.signInUser(prefs.userId)
            .onEach{ onResourceStateless(it, onSucceed) }
            .launchIn(viewModelScope)
    }
    override fun onTimerChange(time: Double){
        _timer.value = time
    }
    override fun postParticipant(participant: Participant) {
        if (participant.isPropsBlank){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        caseParticipation.postParticipant(participant)
            .onEach(::onResourceStateless)
            .launchIn(viewModelScope)
    }
    override fun getParticipant(
        userId: String, roomId: String, onSucceed: (Participant) -> Unit) {
        if (userId.isBlank() || roomId.isBlank()){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        caseParticipation.getParticipant(userId, roomId)
            .onEach{ onResourceStateless(it, onSucceed) }
            .launchIn(viewModelScope)
    }
    override fun patchParticipant(id: String, current: Participant) {
        if (id.isBlank() || current.isPropsBlank){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        current.apply { updatedAt = Date() }
        caseParticipation.patchParticipant(id, current)
            .onEach(this::onResourceStateless).launchIn(viewModelScope)
    }
    override fun deleteParticipant(id: String) {
        if (id.isBlank()){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        caseParticipation.deleteParticipant(id)
            .onEach(this::onResourceStateless).launchIn(viewModelScope)
    }
    override fun getParticipants(page: Int, size: Int) {
        if (size == 0){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        caseParticipation.getParticipants(page, size)
            .onEach(this::onResourceStateless).launchIn(viewModelScope)
    }
}