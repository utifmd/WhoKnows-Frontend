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
import com.dudegenuine.whoknows.ux.compose.state.ResourceState
import com.dudegenuine.whoknows.ux.compose.state.ResourceState.Companion.DONT_EMPTY
import com.dudegenuine.whoknows.ux.compose.state.ScreenState
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
            .onEach{ onResourceParticipation(it, ::onParticipationSucceed, ::onParticipationError) }
            .launchIn(viewModelScope)
    }
    private fun onParticipationSucceed(participated: Participation){
        onTimerChange(prefs.participationTimeLeft.toDouble())

        if (prefs.participationTimeLeft >= 0)
            onParticipationChange(ResourceState.Boarding(data = participated))
        else onPreResult(participated)
    }
    private fun onParticipationError(message: String){
        Log.d(TAG, "onParticipationError: $message")
        savedRoomId?.let { getRoom(it, ::onCreateAnParticipant) }
            ?: onParticipationChange(ResourceState.Boarding(error = "Can\'t recognize the class"))
    }
    private fun onCreateAnParticipant(room: Room.Complete) {
        Log.d(TAG, "onCreateAnParticipant: ${room.id}")
        val participant = caseParticipation.getParticipant().copy(
            roomId = room.id, userId = prefs.userId, timeLeft = room.minute)
        onBoarding(room, participant)

        /*caseParticipant.postParticipant(model).onEach { res ->
            onResource(
                resources = res,
                onSuccess = {
                    caseRoom.timer.start(asSecond)
                    onBoarding(room, it.id)
                },
                onError = { error ->
                    onStateChange(ResourceState(error = ALREADY_JOINED))
                    onShowSnackBar(error)
                }
            )
        }.launchIn(viewModelScope)*/
    }
    private fun onBoarding(room: Room.Complete, participant: Participant){
        /*caseUser.getUser()
            .flatMapConcat{ onResourceFlow(it){ user ->
                val participation = caseParticipation.getParticipation(participantId, room, user)
                caseRoom.postBoarding(participation)
                    .onStart{ onStateChange(ResourceState(participation = participation)) }
                    .mapLatest{ Resource.Success(user) }}}
            .onEach(::onResource)
            .launchIn(viewModelScope)*/

        getCurrentUser{ user ->
            Log.d(TAG, "onBoarding -> getCurrentUser: triggered")
            val participation = caseParticipation.getParticipation(participant.id, room, user)
            val taskDuration = (room.minute.toFloat() * 60).toDouble()

            onParticipationChange(ResourceState.Boarding(data = participation))
            caseRoom.timer.start(taskDuration)

            postBoarding(participation)
        }
    }
    private fun onRemoteParticipation(room: Room.Complete){
        caseUser.getUser(prefs.userId).onEach { res ->
            if(res.data is User.Complete){
                val userComplete = res.data as User.Complete
                val participated = userComplete.participants.find { !it.expired } ?: return@onEach

                val participation = caseParticipation.getParticipation(
                    participantId = participated.id,
                    room = room,
                    currentUser = userComplete)

                if (prefs.participationTimeLeft <= 0) /*is finished*/
                    onPreResult(participation) else onParticipationChange(ResourceState.Boarding(data = participation))
                //onStateChange(ResourceState(participation = participation))
            }
        }.launchIn(viewModelScope)
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
    private fun postBoarding(
        state: Participation, onSucceed: (String) -> Unit) {
        if (state.pages.isEmpty() or state.roomId.isEmpty() or
            state.userId.isEmpty() or state.participantId.isEmpty()) {
            onToast(DONT_EMPTY)
            return
        }
        caseRoom.postBoarding(state)
            .onEach{ onResourceStateless(it, onSucceed)}
            .launchIn(viewModelScope)
    }
    override fun onBoardingActionPressed(index: Int, type: Quiz.Action.Type ) {TODO("Not yet implemented") }
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
    override fun onPreResult(participation: Participation) {
        val questioners = participation.pages
        val correct = questioners.count { it.isCorrect }
        val resultScore = correct.toFloat() / questioners.size.toFloat() * 100
        val event = "${participation.user.username} has joined the ${participation.roomTitle}"
        val result = event.plus(" with score ${resultScore.toDouble()}")
        /*val participant = caseParticipation.getParticipant().copy(
            id = participation.participantId,
            roomId = participation.roomId,
            userId = prefs.userId,
            expired = true,
            currentPage = "${participation.pages.size}",
            timeLeft = 0)
        val notification = formState.notification.copy(
            userId = currentUserId,
            roomId = participation.roomId,
            event = event,
            recipientId = participation.userId)
        val pushNotification = Messaging.Pusher(
            participation.roomTitle, event, participation.user.profileUrl)
        val groupAdder = Messaging.GroupAdder(
            keyName = modelResult.roomId,
            tokens = listOf(prefs.tokenId)
        )*/

        caseRoom.deleteBoarding()
            .onCompletion {
                val checkRoutePoint = Screen.Home.Summary.Participation.routeWithArgs("{$KEY_PARTICIPATION_ROOM_ID}")
                val option = NavOptions.Builder().setPopUpTo(checkRoutePoint, true).build()
                if (it != null) return@onCompletion

                caseRoom.timer.stop()
                onParticipationChange(ResourceState.Boarding(data = null))//onStateChange(ResourceState(participation = null)) //onParticipationCancel()

                onScreenStateChange(ScreenState.Navigate.To(Screen.Home.Summary.route, option))
                onToast(result)
                Log.d(TAG, result)
            }
            .launchIn(viewModelScope)
    }

    override fun onResult(
        roomTitle: String, result: Result, notification: Notification,
        register: Messaging.GroupAdder, pusher: Messaging.Pusher){

        /*
        Log.d(TAG, "onResult: triggered")
        val route = Screen.Home.Summary.Participation.Result.route

        flowOf(
            caseRoom.deleteBoarding(),
            caseResult.postResult(result),
            caseNotification.postNotification(notification),
            flowRegisterPushMessaging(result.roomId, register, pusher)).flattenMerge()
            //.onStart { onStateChange(ResourceState(participation = null)) }
            .onEach(::onResource)
            .onCompletion {
                it?.let(::onResolveAddMessaging) ?:
                onScreenStateChange(ScreenState.Navigate.To(route))
            }
            .catch { it.let(::onResolveAddMessaging) }
            .launchIn(viewModelScope)*/
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
    override fun onTimerChange(time: Double){
        _timer.value = time
    }
    override fun postParticipant(participant: Participant) {
        if (participant.isPropsBlank){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        participant.apply { createdAt = Date() }
        caseParticipation.postParticipant(participant)
            .onEach(::onResourceStateless).launchIn(viewModelScope)
    }
    override fun getParticipant(id: String) {
        if (id.isBlank()){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        caseParticipation.getParticipant(id)
            .onEach(::onResourceStateless).launchIn(viewModelScope)
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