package com.dudegenuine.whoknows.ui.vm.room

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dudegenuine.local.api.IPrefsFactory
import com.dudegenuine.local.api.IShareLauncher
import com.dudegenuine.local.api.ITimerLauncher
import com.dudegenuine.local.api.ITimerService
import com.dudegenuine.model.*
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.*
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent.Companion.ONBOARD_ROOM_ID_SAVED_KEY
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent.Companion.ROOM_ID_SAVED_KEY
import com.dudegenuine.whoknows.ui.vm.ResourceState
import com.dudegenuine.whoknows.ui.vm.ResourceState.Companion.DESC_TOO_LONG
import com.dudegenuine.whoknows.ui.vm.ResourceState.Companion.DONT_EMPTY
import com.dudegenuine.whoknows.ui.vm.ResourceState.Companion.NO_QUESTION
import com.dudegenuine.whoknows.ui.vm.room.contract.IRoomViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
@HiltViewModel
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class RoomViewModel
    @Inject constructor(
    private val prefsFactory: IPrefsFactory,
    private val caseMessaging: IMessageUseCaseModule,
    private val caseFile: IFileUseCaseModule,
    private val caseNotification: INotificationUseCaseModule,
    private val caseRoom: IRoomUseCaseModule,
    private val caseUser: IUserUseCaseModule,
    private val caseParticipant: IParticipantUseCaseModule,
    private val caseQuiz: IQuizUseCaseModule,
    private val caseResult: IResultUseCaseModule,
    private val savedStateHandle: SavedStateHandle): IRoomViewModel() {
    @Inject lateinit var timer: ITimerLauncher
    @Inject lateinit var share: IShareLauncher
    private val TAG: String = javaClass.simpleName
    val currentUserId get() = prefsFactory.userId

    private val _uiState = MutableLiveData<Room.State>()
    val uiState: LiveData<Room.State>
        get() = _uiState

    private val _formState = mutableStateOf(Room.State.FormState())
    val formState: Room.State.FormState
        get() = _formState.value

    init {
        onUiStateValueChange(
            Room.State.CurrentRoom)

        onDetailRouted()
        onBoardRouted()
        onBoardStored()
    }

    val timerServiceAction = IntentFilter(ITimerService.TIME_ACTION)
    val timerServiceReceiver: (
        Room.State.BoardingQuiz) -> BroadcastReceiver = { roomState ->

        caseRoom.onTimerReceived { time, finished ->
            formState.onTimerChange(time)
            if (finished) onPreResult(roomState)
        }
    }

    private fun onDetailRouted(){
        savedStateHandle.get<String>(ROOM_ID_SAVED_KEY)?.let(this::getRoom)
    }

    private fun onBoardRouted() {
        val roomId = savedStateHandle.get<String>(ONBOARD_ROOM_ID_SAVED_KEY)

        if (!roomId.isNullOrBlank()) onPreBoarding(roomId)
    }

    private fun onBoardStored() {
        if (prefsFactory.participationId.isNotBlank()) caseRoom.getBoarding()
            .onEach { resource -> onResourceBoarding(resource,
                onPrepare = { onUiStateValueChange(Room.State.BoardingPrepare) }) { roomState ->
                    formState.onTimerChange(prefsFactory.runningTime.toDouble())

                    if (prefsFactory.runningTime <= 0) onPreResult(roomState) //is finished
                    else onUiStateValueChange(roomState) }
                }
            .launchIn(viewModelScope)
    }

    private fun onPreBoarding(roomId: String) {
        Log.d(TAG, "onPreBoarding: triggered")
        if (roomId.isBlank())
            onStateChange(ResourceState(error = DONT_EMPTY))

        caseRoom.getRoom(roomId).onEach { res ->
            onResourceSucceed(res, ::onInitBoarding)
        }.launchIn(viewModelScope)
    }

    private fun onInitBoarding(room: Room.Complete) {
        Log.d(TAG, "onInitBoarding: triggered")

        val model = formState.participant.copy(
            roomId = room.id, userId = currentUserId, timeLeft = room.minute)

        val asSecond = (room.minute.toFloat() * 60).toDouble()

        caseParticipant.postParticipant(model).onEach { res ->
            onResource(
                resources = res,
                onSuccess = {
                    timer.start(asSecond)
                    onBoarding(room, it.id)
                },
                onError = { error ->
                    onCloseBoarding()
                    onStateChange(ResourceState(error = ALREADY_JOINED))
                    onShowSnackBar(error)
                }
            )
        }.launchIn(viewModelScope)
    }

    private fun onBoarding(room: Room.Complete, ppnId: String){
        Log.d(TAG, "onBoarding: triggered")
        val quizzes = room.questions.mapIndexed { index, quiz ->

            Room.State.OnBoardingState(
                quiz = quiz,
                questionIndex = index +1,
                totalQuestionsCount = room.questions.size,
                showPrevious = index > 0,
                showDone = index == room.questions.size -1
            )
        }

        if (quizzes.isEmpty()) {
            onStateChange(ResourceState(error = NO_QUESTION))
            return
        }

        getCurrentUser { user ->
            val roomState = Room.State.BoardingQuiz(
                participantId = ppnId,
                userId = room.userId,
                roomId = room.id,
                roomTitle = room.title,
                roomDesc = room.description,
                roomMinute = room.minute,
                quizzes = quizzes,
                participant = user.let { User.Censored(it.id,
                    it.fullName, it.username, it.profileUrl) }
            )

            Log.d(TAG, "onBoarding by: ${roomState.participant.username}")
            postBoarding(roomState) { onUiStateValueChange(roomState) } //_uiState.value = roomState
        }
    }

    fun onPreResult(boardingState: Room.State.BoardingQuiz) {
        val questioners = boardingState.quizzes
        val correct = questioners.count { it.isCorrect }
        val resultScore: Float = correct.toFloat() / questioners.size.toFloat() * 100
        val event = "${boardingState.participant.username} has joined the ${boardingState.roomTitle}"

        val modelResult = formState.result.copy(
           roomId = boardingState.roomId, //participantId = boardingState.participantId,
           userId = currentUserId,
           correctQuiz = questioners.filter { it.isCorrect }.map { it.quiz.question },
           wrongQuiz = questioners.filter { !it.isCorrect }.map { it.quiz.question },
           score = resultScore.toInt())

        val modelNotification = formState.notification.copy(
           userId = currentUserId,
           roomId = boardingState.roomId,
           event = event,
           recipientId = boardingState.userId)

        val modelPushMessaging = Messaging.Pusher(
           largeIcon = boardingState.participant.profileUrl,
           title = boardingState.roomTitle,
           body = event, to = "")

        val modelAddMessaging = Messaging.GroupAdder(
            key = "",
            keyName = modelResult.roomId,
            tokens = listOf(prefsFactory.tokenId)
        )

        Log.d(TAG, "onPreResult: triggered")
        if (formState.hasResultAttempted) return

        formState.onAttemptResult()
        timer.stop()

        onResult(boardingState.roomTitle,
           modelResult, modelNotification, modelAddMessaging, modelPushMessaging)
    }

    private fun onResult(roomTitle: String, result: Result,
        notification: Notification, register: Messaging.GroupAdder, pusher: Messaging.Pusher){
        Log.d(TAG, "onResult: triggered")

        flowOf(
            caseRoom.deleteBoarding(),
            caseResult.postResult(result),
            caseNotification.postNotification(notification),
            flowRegisterPushMessaging(result.roomId, register, pusher)).flattenMerge()
            .onStart { _uiState.postValue(Room.State.BoardingResult(roomTitle, result)) }
            .onCompletion { it?.let(::onResolveAddMessaging) }
            .catch { it.let(::onResolveAddMessaging) }
            .launchIn(viewModelScope)
    }

    fun onCloseBoarding() {
        onUiStateValueChange(Room.State.CurrentRoom) //_uiState.value = Room.RoomState.CurrentRoom
    }

    private fun onUiStateValueChange(roomState: Room.State){
        _uiState.value = roomState
    }

    fun onSharePressed(roomId: String) { //caseRoom.setClipboard("Room ID", roomId)
        val data = "${BuildConfig.BASE_CLIENT_URL}/who-knows/room/$roomId"

        share.launch(data)
    }

    fun onCreatePressed(onSucceed: (Room.Complete) -> Unit) {
        val model = formState.room.copy(userId = currentUserId)

        if (!formState.isPostValid || currentUserId.isBlank()) {
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }
        if (formState.desc.text.length > 225){
            onStateChange(ResourceState(error = DESC_TOO_LONG))
            return
        }
        val messaging = Messaging.GroupCreator(
            model.id, listOf(prefsFactory.tokenId))

        caseMessaging.createMessaging(messaging)
            .flatMapMerge { caseRoom.postRoom(model) }
            .onEach { res -> onResourceSucceed(res, onSucceed) }
            .onCompletion { if (it != null) onResolveCreateMessaging(it, model.id) }
            .catch { it.let(::onResolveCreateMessaging) }
            .launchIn(viewModelScope)

        /*createMessaging() { notifyKey ->
            Log.d(TAG, "onCreatePressed: notifyKey is $notifyKey")

            caseRoom.postRoom(model)
                .onEach { res -> onResourceSucceed(res, onSucceed) }
                .launchIn(viewModelScope)
        }*/
    }

    fun onDeleteRoomPressed(roomId: String, onDeleted: () -> Unit) {
        if (roomId.isBlank()){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        flowOf(
            caseRoom.deleteRoom(roomId),
            caseMessaging.getMessaging(roomId)
                .flatMapConcat { res -> onResourceFlow(res) { key ->
                    val remover = Messaging.GroupRemover(roomId, listOf(prefsFactory.tokenId), key)
                    caseMessaging.removeMessaging(remover) }})
            .flattenMerge()
            .onCompletion { it?.let { onFlowFailed(TAG, it) } ?: onDeleted() }
            .catch { onFlowFailed(TAG, it) }
            .launchIn(viewModelScope)
    }

    fun onDeleteQuestionPressed(quiz: Quiz.Complete) {
        val flowFileIds = quiz.images.map { it.substringAfterLast("/") }

        flowOf(
            caseQuiz.deleteQuiz(quiz.id),
            flowFileIds.asFlow()
                .flatMapConcat { caseFile.deleteFile(it) })
            .flattenMerge()
            .flatMapLatest { caseRoom.getRoom(quiz.roomId) }
            .onEach(::onResource)
            .onCompletion { it?.let(::onResolveAddMessaging) }
            .catch { it.let(::onResolveAddMessaging) }
            .launchIn(viewModelScope)
    }

    fun onDeleteParticipantPressed(participant: Participant) = viewModelScope.launch {
        //val TAG = object{}.javaClass.enclosingMethod?.name
        val room = state.room?.copy() ?: let {
            val message = "invalid current class"
            onStateChange(ResourceState(message = message))
            Log.d(TAG, message)
            return@launch
        }
        flowOf(
            caseParticipant.deleteParticipant(participant.id),
            caseResult.deleteResult(participant.roomId, participant.userId),
            flowUnregisterMessaging(room, participant))
            .flattenMerge()
            .flatMapLatest { caseRoom.getRoom(participant.roomId) } // necessary
            .onEach(::onResource) // necessary
            .onCompletion { it?.let(::onResolveAddMessaging) }
            .catch { it.let(::onResolveAddMessaging) }
            .launchIn(viewModelScope)
    }

    private fun flowUnregisterMessaging(room: Room.Complete, participant: Participant): Flow<Resource<out Any>> {
        val event = "[${participant.user?.username}] just kicked out by [${room.user?.username}] as admin of the ${room.title} class"
        val notifier = formState.notification.copy(
            userId = currentUserId, roomId = participant.roomId,
            event = event, recipientId = participant.userId)
        val remover = Messaging.GroupRemover(participant.roomId, listOf(prefsFactory.tokenId), "key")
        val pusher = Messaging.Pusher(room.title, event, participant.user?.profileUrl ?: "", "key")

        return flowOf(
            caseNotification.postNotification(notifier),
            caseMessaging.getMessaging(participant.roomId)
                .flatMapConcat { res -> onResourceFlow(res) { key ->
                        caseMessaging.pushMessaging(pusher.copy(to = key))
                            .onCompletion { if(it == null) caseMessaging.removeMessaging(remover.copy(key = key)) }
                }
            })
            .flattenMerge()
    }

    override fun postRoom(room: Room.Complete) {
        if (room.isPropsBlank){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        room.apply { createdAt = Date() }

        caseRoom.postRoom(room)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getRoom(id: String) {
        if (id.isBlank()){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        caseRoom.getRoom(id)
            .onEach(::onResource).launchIn(viewModelScope)
    }

    override fun patchRoom(id: String, current: Room.Complete) {
        if (id.isBlank() || current.isPropsBlank){
            onStateChange(ResourceState(error = DONT_EMPTY))
        }

        current.apply { updatedAt = Date() }

        caseRoom.patchRoom(id, current)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    fun expireRoom(current: Room.Complete, finished: () -> Unit) {
        val model = current.copy(expired = true, updatedAt = Date())
        if (model.id.isBlank() || model.isPropsBlank){
            onStateChange(ResourceState(error = DONT_EMPTY))
        }

        caseRoom.patchRoom(model.id, model)
            .onEach{ res -> onResourceSucceed(res) {
                onStateChange(ResourceState(room = it))

                finished() //onSucceed(it)
            }}
            .launchIn(viewModelScope)
    }

    override fun deleteRoom(id: String) {
        if (id.isBlank()){
            onStateChange(ResourceState(error = DONT_EMPTY))
            return
        }

        caseRoom.deleteRoom(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override val rooms: Flow<PagingData<Room.Censored>> =
        caseRoom.getRooms(DEFAULT_BATCH_ROOM)
            .cachedIn(viewModelScope)

    override val roomsOwner: Flow<PagingData<Room.Complete>> =
        caseRoom.getRooms(prefsFactory.userId, DEFAULT_BATCH_ROOM)

    val roomsOwnerDirectly: (String) -> Flow<PagingData<Room.Complete>> = {
        Log.d(TAG, "roomsOwnerDirectly: attempted")

        caseRoom.getRooms(it, DEFAULT_BATCH_ROOM)
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
    }

    override fun getRooms(page: Int, size: Int) {
        if (size == 0){
            onStateChange(ResourceState(error = DONT_EMPTY))
        }

        caseRoom.getRooms(page, size)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    /*val pushMessaging: (String, Messaging.Pusher) -> Flow<Resource<String>> = { roomId, pusher ->
        caseMessaging.getMessaging(roomId).flatMapConcat { res -> onResourceFlow(res) { to ->
            caseMessaging.pushMessaging(pusher.copy(to = to))
        }}
    }*/

    val flowRegisterPushMessaging: (String, Messaging.GroupAdder, Messaging.Pusher) ->
        Flow<Resource<String>> = { roomId, register, pusher ->

        caseMessaging.getMessaging(roomId)
            .flatMapConcat { res -> onResourceFlow(res) { key ->

            caseMessaging.addMessaging(register.copy(key = key))
                .flatMapConcat { onResourceFlow(it) {

                caseMessaging.pushMessaging(pusher.copy(to = key))}
            }}
        }
    }

    private fun onResolveCreateMessaging(t: Throwable, keyName: String? = null) {
        Log.d(TAG, "onResolveCreateMessaging: $keyName ${t.localizedMessage}")

        if (keyName == null) return
        prefsFactory.createMessaging = keyName
    }

    private fun onResolveAddMessaging(t: Throwable) {
        Log.d(TAG, "onResolveRegisterMessaging: ${t.message}")
        prefsFactory.addMessaging = true
    }

    override fun createMessaging(
        messaging: Messaging.GroupCreator, onSucceed: (String) -> Unit) {
        val model = messaging.copy()

        if (model.operation.isBlank() or model.keyName.isBlank() or model.tokens.isEmpty())
            onStateChange(ResourceState(error = DONT_EMPTY))

        caseMessaging.createMessaging(model)
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun addMessaging(
        messaging: Messaging.GroupAdder, onSucceed: (String) -> Unit) {
        val model = messaging.copy()

        if (model.keyName.isBlank() or model.key.isBlank() or model.tokens.isEmpty()) {
            onStateChange(ResourceState(error = DONT_EMPTY))

            return
        }

        caseMessaging.addMessaging(model)
            .onEach { res -> onResourceStateless(res, onSucceed) } //(::onResource)
            .launchIn(viewModelScope)
    }

    private fun getCurrentUser(onSucceed: (User.Complete) -> Unit) {
        caseUser.getUser()
            .onEach{ res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun getBoarding(onSucceed: (Room.State.BoardingQuiz) -> Unit) {
        caseRoom.getBoarding()
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun deleteBoarding(onSucceed: (String) -> Unit) {
        caseRoom.deleteBoarding()
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun patchBoarding(state: Room.State.BoardingQuiz) {
        if (state.quizzes.isEmpty() or state.roomId.isEmpty() or state.userId.isEmpty() or state.participantId.isEmpty())
            onStateChange(ResourceState(error = DONT_EMPTY))

        caseRoom.patchBoarding(state)
            .onEach(::onResource).launchIn(viewModelScope)
    }

    override fun postBoarding(state: Room.State.BoardingQuiz, onSucceed: (String) -> Unit) {
        if (state.quizzes.isEmpty() or state.roomId.isEmpty() or state.userId.isEmpty() or state.participantId.isEmpty())
            onStateChange(ResourceState(error = DONT_EMPTY))

        caseRoom.postBoarding(state)
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }
}