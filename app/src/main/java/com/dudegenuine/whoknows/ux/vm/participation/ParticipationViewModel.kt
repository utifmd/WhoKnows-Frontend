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

    private val _timer = mutableStateOf(0.0)
    val timer get() = _timer.value

    private val prefs get() = caseParticipation.prefs
    val receiver get() = caseRoom.receiver

    /*private val _isParticipated = mutableStateOf(prefs.participationId.isNotBlank())
    val isParticipated get() = _isParticipated.value

    private fun onParticipationCancel(){
        Log.d(TAG, "onParticipationCancel: ")
        prefs.participationId = ""
        _isParticipated.value = false
    }*/

    init {
        onParticipationRouted()
        onParticipationStored()
    }
    private fun onParticipationRouted(){
        val roomId = savedStateHandle.get<String>(KEY_PARTICIPATION_ROOM_ID) ?: return
        if (roomId.isBlank()) return
        Log.d(TAG, "onParticipationRouted: triggered")
        getRoom(roomId, ::onCreateAnParticipant)
    }
    private fun onParticipationStored(){
        if (prefs.participationId.isNotBlank()) caseRoom.getBoarding()
            .onEach{ onResourceSucceed(it) { participation ->
                onTimerChange(prefs.runningTime.toDouble())

                if (prefs.runningTime <= 0) onPreResult(participation) //is finished
                else onStateChange(ResourceState(participation = participation))
            }}
            .launchIn(viewModelScope)
    }
    private fun onCreateAnParticipant(room: Room.Complete) {
        Log.d(TAG, "onCreateAnParticipant: ${room.id}")
        val taskDuration = (room.minute.toFloat() * 60).toDouble()
        val participant = caseParticipation.getParticipant()
            .copy(roomId = room.id, userId = prefs.userId, timeLeft = room.minute)
        caseRoom.timer.start(taskDuration)
        onBoarding(room, participant.id)

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
    private fun onBoarding(room: Room.Complete, participantId: String){
        /*caseUser.getUser()
            .flatMapConcat{ onResourceFlow(it){ user ->
                val participation = caseParticipation.getParticipation(participantId, room, user)
                caseRoom.postBoarding(participation)
                    .onStart{ onStateChange(ResourceState(participation = participation)) }
                    .mapLatest{ Resource.Success(user) }}}
            .onEach(::onResource)
            .launchIn(viewModelScope)*/

        getCurrentUser { user ->
            val participation = caseParticipation.getParticipation(participantId, room, user)

            onStateChange(ResourceState(participation = participation))
            postBoarding(participation)
        }
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
    override fun onBoardingActionPressed(index: Int, type: Quiz.Action.Type ) {TODO("Not yet implemented") }
    override fun onBoardingBackPressed() = onScreenStateChange(ScreenState.Navigate.Back)
    override fun onBoardingPrevPressed() {
        val participation = state.participation ?: return
        val fresh = participation.apply{ currentQuestionIdx -= 1 }
        onStateChange(ResourceState(participation = fresh))
    }
    override fun onBoardingNextPressed() {
        val participation = state.participation ?: return
        val fresh = participation.apply{ currentQuestionIdx += 1 }
        onStateChange(ResourceState(participation = fresh))
    }
    override fun onBoardingDonePressed() {
        val participation = state.participation ?: return
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
        val resultScore: Float = correct.toFloat() / questioners.size.toFloat() * 100
        val event = "${participation.user.username} has joined the ${participation.roomTitle}"

        /*val modelResult = formState.result.copy(
            roomId = participation.roomId, //participantId = boardingState.participantId,
            userId = currentUserId,
            correctQuiz = questioners.filter { it.isCorrect }.map { it.quiz.question },
            wrongQuiz = questioners.filter { !it.isCorrect }.map { it.quiz.question },
            score = resultScore.toInt())

        val modelNotification = formState.notification.copy(
            userId = currentUserId,
            roomId = participation.roomId,
            event = event,
            recipientId = participation.userId)

        val modelPushMessaging = Messaging.Pusher(
            participation.roomTitle, event, participation.user.profileUrl)

        val modelAddMessaging = Messaging.GroupAdder(
            keyName = modelResult.roomId,
            tokens = listOf(prefs.tokenId)
        )*/

        caseRoom.deleteBoarding()
            .onCompletion {
                if (it != null) return@onCompletion

                caseRoom.timer.stop()
                onStateChange(ResourceState(participation = null)) //onParticipationCancel()

                val home = Screen.Home.Summary.route
                onNavigateBack()
                onNavigateTo(home)
                onToast(event.plus(" with score ${resultScore.toDouble()}"))
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
            .onEach { res -> onResourceSucceed(res, onSucceed) }
            .launchIn(viewModelScope)
    }
    override fun getCurrentUser(onSucceed: (User.Complete) -> Unit) {
        caseUser.getUser()
            .onEach{ res -> onResourceStateless(res, onSucceed) }
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
            .onEach(this::onResource).launchIn(viewModelScope)
    }
    override fun getParticipant(id: String) {
        if (id.isBlank()){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        caseParticipation.getParticipant(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }
    override fun patchParticipant(id: String, current: Participant) {
        if (id.isBlank() || current.isPropsBlank){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        current.apply { updatedAt = Date() }
        caseParticipation.patchParticipant(id, current)
            .onEach(this::onResource).launchIn(viewModelScope)
    }
    override fun deleteParticipant(id: String) {
        if (id.isBlank()){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        caseParticipation.deleteParticipant(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }
    override fun getParticipants(page: Int, size: Int) {
        if (size == 0){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        caseParticipation.getParticipants(page, size)
            .onEach(this::onResource).launchIn(viewModelScope)
    }
}