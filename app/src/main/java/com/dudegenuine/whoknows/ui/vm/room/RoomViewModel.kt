package com.dudegenuine.whoknows.ui.vm.room

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dudegenuine.local.api.ITimerService.Companion.INITIAL_TIME_KEY
import com.dudegenuine.local.api.ITimerService.Companion.TIME_ACTION
import com.dudegenuine.local.api.ITimerService.Companion.TIME_UP_KEY
import com.dudegenuine.model.Messaging
import com.dudegenuine.model.Result
import com.dudegenuine.model.Room
import com.dudegenuine.model.User
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
@HiltViewModel
class RoomViewModel
    @Inject constructor(
    private val caseMessaging: IMessageUseCaseModule,
    private val caseFile: IFileUseCaseModule,
    private val caseRoom: IRoomUseCaseModule,
    private val caseUser: IUserUseCaseModule,
    private val caseParticipant: IParticipantUseCaseModule,
    private val caseResult: IResultUseCaseModule,
    private val savedStateHandle: SavedStateHandle): BaseViewModel(), IRoomViewModel {
    private val TAG: String = javaClass.simpleName

    private val currentUserId = caseRoom.currentUserId()
    private val currentToken = caseRoom.currentToken()
    private val currentRunningTime = caseRoom.currentRunningTime()

    private val _uiState = MutableLiveData<Room.RoomState>()
    val uiState: LiveData<Room.RoomState>
        get() = _uiState

    private val _formState = mutableStateOf(Room.RoomState.FormState())
    val formState: Room.RoomState.FormState
        get() = _formState.value

    init {
        val routed = savedStateHandle.get<String>(ROOM_ID_SAVED_KEY)

        if (routed != null) routed.let(this::getRoom)
        else getRooms(currentUserId)

        onBoardRouted()
        onBoardStored()
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

        val model = formState.participantModel.copy(
            roomId = room.id, userId = currentUserId, timeLeft = room.minute)

        onBoarding(room, model.id)

        /*caseRoom.postParticipant(model).onEach { res ->
            onResourceSucceed(res) {
                onBoarding(room, it.id)

                onBoardingValueChange(it.roomId, it.id)
            }
        }.launchIn(viewModelScope)*/
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
            val roomState = Room.RoomState.BoardingQuiz (
                participantId = ppnId,
                participantName = user.username,
                roomId = room.id,
                roomTitle = room.title,
                roomDesc = room.description,
                roomMinute = room.minute,
                quizzes = quizzes
            )

            Log.d(TAG, "onBoarding by: ${roomState.participantName}")
            postBoarding(roomState)
                { onUiStateValueChange(roomState) } //_uiState.value = roomState
        }
    }

    fun onPreResult(boardingState: Room.RoomState.BoardingQuiz) {
        val questioners = boardingState.quizzes
        val correct = questioners.count { it.isCorrect }
        val incorrect = questioners.count { !it.isCorrect }
        val resultScore: Float = correct.toFloat() / questioners.size.toFloat() * 100

        val result = Result(
            id = "RSL-${UUID.randomUUID()}",
            roomId = boardingState.roomId,
            participantId = boardingState.participantId,
            userId = currentUserId,
            correctQuiz = questioners.filter { it.isCorrect }.map { it.quiz.question },
            wrongQuiz = questioners.filter { !it.isCorrect }.map { it.quiz.question },
            score = resultScore.toInt(),
            createdAt = Date(),
            updatedAt = null)

        deleteBoarding {
            onUiStateValueChange(
                Room.RoomState.BoardingResult(boardingState.roomTitle, result))//_uiState.value = Room.RoomState.BoardingResult(boardingState.roomTitle, result)

            /*caseRoom.postResult(result).onEach { res -> onResourceSucceed(res)
                { _uiState.value = RoomState.BoardingResult(boardingState.roomTitle, it) }
            }*/
        }

        getMessagingGroupKey(result.roomId) { groupKey ->
            addMessagingGroupMember(Messaging.GroupAdder(
                keyName = result.roomId,
                key = groupKey,
                tokens = listOf(caseRoom.currentToken()))) {
                Log.d(TAG, "addMessagingGroupMember: triggered")

                pushMessaging(Messaging.Pusher(
                    title = boardingState.roomTitle,
                    body = "${boardingState.participantName} has joined the room",
                    to = groupKey)) {

                    Log.d(TAG, "pushMessaging: triggered")
                }
            }

            Log.d(TAG, "getMessagingGroupKey: triggered")
        }

        Log.d(TAG, "onPreResult: triggered")
    }

    fun onCloseResult() {
        onUiStateValueChange(Room.RoomState.CurrentRoom) //_uiState.value = Room.RoomState.CurrentRoom
    }

    private fun onUiStateValueChange(roomState: Room.RoomState){
        _uiState.value = roomState
    }

    fun onClipboardPressed(roomId: String) {
        caseRoom.setClipboard("Room ID", roomId)
        Log.d(TAG, "onClipboardPressed: triggered")
    }

    fun onCreatePressed(onSucceed: (Room) -> Unit) {
        val model = formState.model.copy(userId = currentUserId)

        if (!formState.isPostValid || currentUserId.isBlank()) {
            _state.value = ResourceState(error = DONT_EMPTY)

            return
        }

        if (formState.desc.text.length > 225){
            _state.value = ResourceState(error = DESC_TOO_LONG)

            return
        }

        createMessagingGroup(Messaging.GroupCreator(
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

    fun deleteRoom(id: String, onSucceed: (String) -> Unit) {
        if (id.isBlank()){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        caseRoom.deleteRoom(id)
            .onEach { res -> onResourceSucceed(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun getRooms(page: Int, size: Int) {
        if (size == 0){
            _state.value = ResourceState(error = DONT_EMPTY)
        }

        caseRoom.getRooms(page, size)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getRooms(userId: String) {
        if (userId.isBlank())
            _state.value = ResourceState(error = DONT_EMPTY)

        caseRoom.getRooms(userId)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getMessagingGroupKey(keyName: String, onSucceed: (String) -> Unit) {
        if (keyName.isBlank()) _state.value = ResourceState(error = DONT_EMPTY)

        caseMessaging.getMessaging(keyName)
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun createMessagingGroup(messaging: Messaging.GroupCreator, onSucceed: (String) -> Unit) {
        val model = messaging.copy()
        if (model.operation.isBlank() or model.keyName.isBlank() or model.tokens.isEmpty())
            _state.value = ResourceState(error = DONT_EMPTY)

        caseMessaging.createMessaging(model)
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    override fun addMessagingGroupMember(
        messaging: Messaging.GroupAdder, onSucceed: (String) -> Unit) {
        val model = messaging.copy()

        if (model.keyName.isBlank() or model.key.isBlank() or model.tokens.isEmpty())
            _state.value = ResourceState(error = DONT_EMPTY)

        caseMessaging.createMessaging(model)
            .onEach { res -> onResourceStateless(res, onSucceed) } //(::onResource)
            .launchIn(viewModelScope)
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
        if (state.quizzes.isEmpty() or state.roomId.isEmpty() or state.participantId.isEmpty())
            _state.value = ResourceState(error = DONT_EMPTY)

        caseRoom.patchBoarding(state)
            .onEach(::onResource).launchIn(viewModelScope)
    }

    override fun postBoarding(state: Room.RoomState.BoardingQuiz, onSucceed: (String) -> Unit) {
        if (state.quizzes.isEmpty() or state.roomId.isEmpty() or state.participantId.isEmpty())
            _state.value = ResourceState(error = DONT_EMPTY)

        caseRoom.postBoarding(state)
            .onEach { res -> onResourceStateless(res, onSucceed) }
            .launchIn(viewModelScope)
    }

    val timerServiceAction = IntentFilter(TIME_ACTION)

    val timerServiceReceiver: (Room.RoomState.BoardingQuiz) -> BroadcastReceiver = { roomState ->
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                val time = intent.getDoubleExtra(INITIAL_TIME_KEY, 0.0)
                val finished = intent.getBooleanExtra(TIME_UP_KEY, false)

                formState.onTimerChange(time)
                if (finished) onPreResult(roomState)
            }
        }
    }
}