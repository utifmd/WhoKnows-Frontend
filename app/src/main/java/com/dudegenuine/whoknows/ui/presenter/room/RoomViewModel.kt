package com.dudegenuine.whoknows.ui.presenter.room

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.Result
import com.dudegenuine.model.Room
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IFileUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IParticipantUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IResultUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IRoomUseCaseModule
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent.Companion.ONBOARD_ROOM_ID_SAVED_KEY
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent.Companion.ROOM_ID_SAVED_KEY
import com.dudegenuine.whoknows.ui.compose.state.OnBoardingState
import com.dudegenuine.whoknows.ui.compose.state.RoomState
import com.dudegenuine.whoknows.ui.presenter.BaseViewModel
import com.dudegenuine.whoknows.ui.presenter.ResourceState
import com.dudegenuine.whoknows.ui.presenter.ResourceState.Companion.DESC_TOO_LONG
import com.dudegenuine.whoknows.ui.presenter.ResourceState.Companion.DONT_EMPTY
import com.dudegenuine.whoknows.ui.presenter.ResourceState.Companion.NO_QUESTION
import com.dudegenuine.whoknows.ui.presenter.room.contract.IRoomViewModel
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
        private val caseRoom: IRoomUseCaseModule,
        private val caseParticipant: IParticipantUseCaseModule,
        private val caseResult: IResultUseCaseModule,
        private val caseFile: IFileUseCaseModule,
        private val savedStateHandle: SavedStateHandle): BaseViewModel(), IRoomViewModel {

    private val TAG: String = javaClass.simpleName
    private val _uiState = MutableLiveData<RoomState>()
    val uiState: LiveData<RoomState>
        get() = _uiState

    private val _formState = mutableStateOf(RoomState.FormState())
    val formState: RoomState.FormState
        get() = _formState.value

    init {
        _uiState.value = RoomState.CurrentRoom // soon being removed

        val navigated = savedStateHandle.get<String>(ROOM_ID_SAVED_KEY)

        if (navigated != null) navigated.let(this::getRoom)
        else getRooms(caseRoom.currentUserId())

        onBoardNavigated()
        onBoardSaved()
    }

    private fun onBoardNavigated() {
        val roomId = savedStateHandle.get<String>(ONBOARD_ROOM_ID_SAVED_KEY) //val ppnId = savedStateHandle.get<String>(ONBOARD_PPN_ID_SAVED_KEY)

        if (!roomId.isNullOrBlank() /* && !ppnId.isNullOrBlank()*/)
            onPreBoarding(roomId)
    }

    private fun onBoardSaved() {
        val roomId = caseRoom.getterOnboard.roomId()
        val ppnId = caseRoom.getterOnboard.participantId()

        if (ppnId.isNotBlank() && roomId.isNotBlank())
            onPreBoarding(roomId, ppnId)

        Log.d(TAG, "onBoardSaved: done")
    }

    private fun onPreBoarding(roomId: String, ppnId: String? = null) {
        Log.d(TAG, "onPreBoarding: triggered")

        if (roomId.isBlank())
            _state.value = ResourceState(error = DONT_EMPTY)

        caseRoom.getRoom(roomId).onEach { resRoom ->
            onResourceSucceed(resRoom) { room ->
                if (ppnId.isNullOrBlank()) onInitBoarding(room)
                else onBoarding(room, ppnId)
            }
        }.launchIn(viewModelScope)
    }

    private fun onInitBoarding(room: Room) {
        Log.d(TAG, "onInitBoarding: triggered")

        val model = formState.participantModel.copy(
            roomId = room.id, userId = caseRoom.currentUserId(), timeLeft = room.minute
        )

        caseParticipant.postParticipant(model).onEach { res ->
            onResourceSucceed(res) {
                onBoarding(room, it.id)

                onBoardingValueChange(it.roomId, it.id)
            }
        }.launchIn(viewModelScope)
    }

    private fun onBoarding(room: Room, ppnId: String){
        val quizzes = room.questions.mapIndexed { index, quiz ->
            OnBoardingState(
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

        val roomState = RoomState.BoardingQuiz(
            participantId = ppnId,
            room = room,
            list = quizzes
        )

        _uiState.value = roomState
    }

    fun onPreResult(boardingState: RoomState.BoardingQuiz) {
        val questioners = boardingState.list

        val correct = questioners.count { it.isCorrect }
        val incorrect = questioners.count { !it.isCorrect }

        val resultScore: Float = correct.toFloat() / questioners.size.toFloat() * 100

        Log.d(TAG, "computeResult: begin")
        Log.d(TAG, "total questions: ${questioners.size}")
        Log.d(TAG, "correct count: $correct")
        Log.d(TAG, "incorrect count: $incorrect")
        Log.d(TAG, "result score: $resultScore")

        val result = Result(
            id = "RSL-${UUID.randomUUID()}",
            roomId = boardingState.room.id,
            participantId = boardingState.participantId,
            userId = caseRoom.currentUserId(),
            correctQuiz = questioners.filter { it.isCorrect }.map { it.quiz.question },
            wrongQuiz = questioners.filter { !it.isCorrect }.map { it.quiz.question },
            score = resultScore.toInt(),
            createdAt = Date(),
            updatedAt = null
        )

        Log.d(TAG, "computeResult: $result")
        onBoardingValueChange("", "")

        caseResult.postResult(result).onEach { res -> onResourceSucceed(res)
            { _uiState.value = RoomState.BoardingResult(boardingState.room.title, it) }
        }
    }

    fun shareResult() {
        TODO("Not yet implemented")
    }

    fun onCloseResult() {
        _uiState.value = RoomState.CurrentRoom

        onBoardingValueChange("", "")
    }

    private fun onBoardingValueChange(roomId: String, ppnId: String) {
        caseRoom.setterOnboard.apply {
            roomId(roomId)
            participantId(ppnId)
        }
    }

    fun onClipboardPressed(roomId: String) {
        caseRoom.saveInClipboard("Room ID", roomId)
        Log.d(TAG, "onClipboardPressed: triggered")
    }

    fun onCreatePressed(onSucceed: (Room) -> Unit) {
        val model = formState.model.copy(userId = caseRoom.currentUserId())

        if (!formState.isPostValid || caseRoom.currentUserId().isBlank()) {
            _state.value = ResourceState(error = DONT_EMPTY)

            return
        }

        if (formState.desc.text.length > 225){
            _state.value = ResourceState(error = DESC_TOO_LONG)

            return
        }

        caseRoom.postRoom(model)
            .onEach { res -> onResourceSucceed(res, onSucceed) }
            .launchIn(viewModelScope)
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
}