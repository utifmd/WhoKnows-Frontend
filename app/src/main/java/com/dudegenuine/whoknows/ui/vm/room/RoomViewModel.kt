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
import com.dudegenuine.local.api.IShareLauncher
import com.dudegenuine.local.api.ITimerLauncher
import com.dudegenuine.local.api.ITimerService
import com.dudegenuine.model.*
import com.dudegenuine.whoknows.infrastructure.common.Constants.BASE_CLIENT_URL
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.*
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent.Companion.ONBOARD_ROOM_ID_SAVED_KEY
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent.Companion.ROOM_ID_SAVED_KEY
import com.dudegenuine.whoknows.ui.vm.BaseViewModel
import com.dudegenuine.whoknows.ui.vm.ResourceState
import com.dudegenuine.whoknows.ui.vm.ResourceState.Companion.DESC_TOO_LONG
import com.dudegenuine.whoknows.ui.vm.ResourceState.Companion.DONT_EMPTY
import com.dudegenuine.whoknows.ui.vm.ResourceState.Companion.NO_QUESTION
import com.dudegenuine.whoknows.ui.vm.ResourceState.Companion.PUSH_NOT_SENT
import com.dudegenuine.whoknows.ui.vm.room.contract.IRoomViewModel
import com.dudegenuine.whoknows.ui.vm.room.contract.IRoomViewModel.Companion.DEFAULT_BATCH_ROOM
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
@ExperimentalCoroutinesApi
@FlowPreview
@HiltViewModel
class RoomViewModel
    @Inject constructor(
    private val caseMessaging: IMessageUseCaseModule,
    private val caseFile: IFileUseCaseModule,
    private val caseNotification: INotificationUseCaseModule,
    private val caseRoom: IRoomUseCaseModule,
    private val caseUser: IUserUseCaseModule,
    private val caseParticipant: IParticipantUseCaseModule,
    private val caseQuiz: IQuizUseCaseModule,
    private val caseResult: IResultUseCaseModule,
    private val savedStateHandle: SavedStateHandle): BaseViewModel(), IRoomViewModel {
    @Inject lateinit var timer: ITimerLauncher
    @Inject lateinit var share: IShareLauncher

    private val TAG: String = javaClass.simpleName
    val currentUserId = caseRoom.currentUserId()
    private val currentToken = caseRoom.currentToken()
    private val currentRunningTime = caseRoom.currentRunningTime()

    private val _uiState = MutableLiveData<Room.RoomState>()
    val uiState: LiveData<Room.RoomState>
        get() = _uiState

    private val _formState = mutableStateOf(Room.RoomState.FormState())
    val formState: Room.RoomState.FormState
        get() = _formState.value

    init {
        onUiStateValueChange(
            Room.RoomState.CurrentRoom)

        onDetailRouted()
        onBoardRouted()
        onBoardStored()
    }

    private fun onDetailRouted(){
        savedStateHandle.get<String>(ROOM_ID_SAVED_KEY)?.let(this::getRoom)
        //else getRooms(currentUserId, DEFAULT_BATCH_ROOM)
    }

    private fun onBoardRouted() {
        val roomId = savedStateHandle.get<String>(ONBOARD_ROOM_ID_SAVED_KEY)

        if (!roomId.isNullOrBlank()) onPreBoarding(roomId)
    }

    private fun onBoardStored() {
        getBoarding { roomState ->
            if (currentRunningTime.isNotBlank()) currentRunningTime.let {
                val time = it.toDouble()
                formState.onTimerChange(time)

                if (time <= 0.0) onPreResult(roomState) //is finished
            }
            onUiStateValueChange(roomState)
        }
    }

    private fun onPreBoarding(roomId: String) {
        Log.d(TAG, "onPreBoarding: triggered")
        if (roomId.isBlank())
            _state.value = ResourceState(error = DONT_EMPTY)

        caseRoom.getRoom(roomId).onEach { res ->
            onResourceSucceed(res, ::onInitBoarding)
        }.launchIn(viewModelScope)
    }

    private fun onInitBoarding(room: Room) {
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
                onError = {
                    onCloseBoarding()
                    _state.value = ResourceState(error = it)
                }
            )
        }.launchIn(viewModelScope)
    }

    private fun onBoarding(room: Room, ppnId: String){
        Log.d(TAG, "onBoarding: triggered")
        val quizzes = room.questions.mapIndexed { index, quiz ->

            Room.RoomState.OnBoardingState(
                quiz = quiz,
                questionIndex = index +1,
                totalQuestionsCount = room.questions.size,
                showPrevious = index > 0,
                showDone = index == room.questions.size -1
            )
        }

        if (quizzes.isEmpty()) {
            _state.value = ResourceState(error = NO_QUESTION)
            return
        }

        getCurrentUser { user ->
            val roomState = Room.RoomState.BoardingQuiz(
                participantId = ppnId,
                participant = user.let { UserCensored(
                    it.id, it.fullName, it.username, it.profileUrl) },
                userId = room.userId,
                roomId = room.id,
                roomTitle = room.title,
                roomDesc = room.description,
                roomMinute = room.minute,
                quizzes = quizzes
            )
            postBoarding(roomState) { onUiStateValueChange(roomState) } //_uiState.value = roomState

            Log.d(TAG, "onBoarding by: ${roomState.participant.username}")
        }
    }

    fun onPreResult(boardingState: Room.RoomState.BoardingQuiz) {
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

       val modelMessaging = Messaging.Pusher(
            largeIcon = boardingState.participant.profileUrl,
            title = boardingState.roomTitle,
            body = event, to = "")

       timer.stop()
       Log.d(TAG, "onPreResult: triggered")

       onResult(boardingState.roomTitle,
           modelResult, modelNotification, modelMessaging)
    }

    private fun onResult(roomTitle: String,
        modelResult: Result, modelNotification: Notification, modelMessaging: Messaging.Pusher){

        flowOf(
            caseResult.postResult(modelResult),
            caseNotification.postNotification(modelNotification))
            .flattenMerge()
            .onEach { onResource(it) }
            .onCompletion {
                deleteBoarding { Log.d(TAG, "delete boarding: $it") }
                getMessaging(modelResult.roomId) { groupKey ->
                    addMessaging(Messaging.GroupAdder(
                        key = groupKey, keyName = modelResult.roomId, tokens = listOf(caseRoom.currentToken()))) {

                        pushMessaging(modelMessaging.copy(to = groupKey))
                            { Log.d(TAG, "pushMessaging: triggered") }
                    }
                }
                _uiState.postValue(
                    Room.RoomState.BoardingResult(roomTitle, modelResult))
                Log.d(TAG, "finally: triggered") }
            .launchIn(viewModelScope)
    }

    fun onCloseBoarding() {
        onUiStateValueChange(Room.RoomState.CurrentRoom) //_uiState.value = Room.RoomState.CurrentRoom
    }

    private fun onUiStateValueChange(roomState: Room.RoomState){
        _uiState.value = roomState
    }

    fun onSharePressed(roomId: String) { //caseRoom.setClipboard("Room ID", roomId)
        val data = "$BASE_CLIENT_URL/who-knows/room/$roomId"

        share.launch(data)
    }

    val timerServiceAction = IntentFilter(ITimerService.TIME_ACTION)

    val timerServiceReceiver: (
        Room.RoomState.BoardingQuiz) -> BroadcastReceiver = { roomState ->

        caseRoom.onTimerThick { time, finished ->
            formState.onTimerChange(time)
            if (finished) onPreResult(roomState)
        }
    }

    fun onCreatePressed(onSucceed: (Room) -> Unit) {
        val model = formState.room.copy(userId = currentUserId)

        if (!formState.isPostValid || currentUserId.isBlank()) {
            _state.value = ResourceState(error = DONT_EMPTY)

            return
        }

        if (formState.desc.text.length > 225){
            _state.value = ResourceState(error = DESC_TOO_LONG)

            return
        }

        createMessaging(Messaging.GroupCreator(
            keyName = model.id,
            tokens = listOf(currentToken))){ notifyKey ->
            Log.d(TAG, "onCreatePressed: notifyKey is $notifyKey")

            caseRoom.postRoom(model)
                .onEach { res -> onResourceSucceed(res, onSucceed) }
                .launchIn(viewModelScope)
        }
    }

    override fun postRoom(room: Room) {
        if (room.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        room.apply { createdAt = Date() }

        caseRoom.postRoom(room)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getRoom(id: String) {
        if (id.isBlank()){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        caseRoom.getRoom(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun patchRoom(id: String, current: Room) {
        if (id.isBlank() || current.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
        }

        current.apply { updatedAt = Date() }

        caseRoom.patchRoom(id, current)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    fun expireRoom(current: Room, onSucceed: (Room) -> Unit) {
        val model = current.copy(expired = true, updatedAt = Date())
        if (model.id.isBlank() || model.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
        }

        caseRoom.patchRoom(model.id, model)
            .onEach{ res -> onResourceSucceed(res) {
                _state.value = ResourceState(room = it)

                onSucceed(it)
            }}
            .launchIn(viewModelScope)
    }

    override fun deleteRoom(id: String) {
        if (id.isBlank()){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        caseRoom.deleteRoom(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    fun deleteRoom(roomId: String, onSucceed: (String) -> Unit) {
        if (roomId.isBlank()){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        caseRoom.deleteRoom(roomId)
            .onEach { res -> onResourceSucceed(res) {

                getMessaging(roomId) { groupKey ->
                    val group = Messaging.GroupRemover(roomId, listOf(currentToken), groupKey)

                    removeMessaging(group) { Log.d(TAG, "deleted: group $it") }
                }
                onSucceed(roomId)
            }}
            .launchIn(viewModelScope)
    }

    fun deleteParticipation(
        participant: Participant, onSucceed: () -> Unit) = viewModelScope.launch {

        flowOf(
            caseParticipant.deleteParticipant(participant.id),
            caseNotification.deleteNotification(participant.roomId, participant.userId),
            caseResult.deleteResult(participant.roomId, participant.userId))
            .flattenMerge()
            .onEach(::onResource)
            .onCompletion { onSucceed() }
            .launchIn(viewModelScope)
    }

    fun deleteQuestion(quiz: Quiz, onSucceed: () -> Unit) {
        val deleteFiles = quiz.images.map {
            val fileId = it.substringAfterLast("/")
            caseFile.deleteFile(fileId)
        }

        flowOf(deleteFiles.merge(), caseQuiz.deleteQuiz(quiz.id))
            .flattenMerge()
            .onEach(::onResource)
            .onCompletion { onSucceed() }
            .launchIn(viewModelScope)
    }

    override val rooms: Flow<PagingData<Room>> =
        caseRoom.getRooms(DEFAULT_BATCH_ROOM).cachedIn(viewModelScope)

    override val roomsOwner: Flow<PagingData<Room>> =
        caseRoom.getRooms(currentUserId, DEFAULT_BATCH_ROOM)
            .cachedIn(viewModelScope)

    override fun getRooms(page: Int, size: Int) {
        if (size == 0){
            _state.value = ResourceState(error = DONT_EMPTY)
        }

        caseRoom.getRooms(page, size)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getMessaging(keyName: String, onSucceed: (String) -> Unit) {
        if (keyName.isBlank()) _state.value = ResourceState(error = DONT_EMPTY)

        caseMessaging.getMessaging(keyName)
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun createMessaging(
        messaging: Messaging.GroupCreator, onSucceed: (String) -> Unit) {
        val model = messaging.copy()

        if (model.operation.isBlank() or model.keyName.isBlank() or model.tokens.isEmpty())
            _state.value = ResourceState(error = DONT_EMPTY)

        caseMessaging.createMessaging(model)
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun addMessaging(
        messaging: Messaging.GroupAdder, onSucceed: (String) -> Unit) {
        val model = messaging.copy()

        if (model.keyName.isBlank() or model.key.isBlank() or model.tokens.isEmpty()) {
            _state.value = ResourceState(error = DONT_EMPTY)

            return
        }

        caseMessaging.addMessaging(model)
            .onEach { res -> onResourceStateless(res, onSucceed) } //(::onResource)
            .launchIn(viewModelScope)

        Log.d(TAG, "addMessagingGroupMember: triggered")
    }

    override fun pushMessaging(
        messaging: Messaging.Pusher, onSucceed: (String) -> Unit) {
        val model = messaging.copy()

        if (model.title.isBlank() or model.body.isBlank() or model.to.isBlank())
            _state.value = ResourceState(error = PUSH_NOT_SENT)

        caseMessaging.pushMessaging(model)
            .onEach { res -> onResourceStateless(res, onSucceed) }//(::onResource)
            .launchIn(viewModelScope)
    }

    private fun getCurrentUser(onSucceed: (User) -> Unit) {
        caseUser.getUser()
            .onEach{ res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun getBoarding(onSucceed: (Room.RoomState.BoardingQuiz) -> Unit) {
        caseRoom.getBoarding()
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun deleteBoarding(onSucceed: (String) -> Unit) {
        caseRoom.deleteBoarding()
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun patchBoarding(state: Room.RoomState.BoardingQuiz) {
        if (state.quizzes.isEmpty() or state.roomId.isEmpty() or state.userId.isEmpty() or state.participantId.isEmpty())
            _state.value = ResourceState(error = DONT_EMPTY)

        caseRoom.patchBoarding(state)
            .onEach(::onResource).launchIn(viewModelScope)
    }

    override fun postBoarding(state: Room.RoomState.BoardingQuiz, onSucceed: (String) -> Unit) {
        if (state.quizzes.isEmpty() or state.roomId.isEmpty() or state.userId.isEmpty() or state.participantId.isEmpty())
            _state.value = ResourceState(error = DONT_EMPTY)

        caseRoom.postBoarding(state)
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }
}