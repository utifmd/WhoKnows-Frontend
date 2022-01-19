package com.dudegenuine.whoknows.ui.presenter.room

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.Room
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IRoomUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUserUseCaseModule
import com.dudegenuine.whoknows.ui.compose.state.OnBoardingState
import com.dudegenuine.whoknows.ui.compose.state.RoomState
import com.dudegenuine.whoknows.ui.presenter.BaseViewModel
import com.dudegenuine.whoknows.ui.presenter.ResourceState
import com.dudegenuine.whoknows.ui.presenter.ResourceState.Companion.DONT_EMPTY
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
    private val case: IRoomUseCaseModule,
    private val userCase: IUserUseCaseModule,
    private val savedStateHandle: SavedStateHandle): BaseViewModel(), IRoomViewModel {
    private val TAG: String = javaClass.simpleName

    private val _uiState = MutableLiveData<RoomState>()
    val uiState: LiveData<RoomState>
        get() = _uiState

    private val _formState = mutableStateOf(RoomState.FormState())
    val formState: RoomState.FormState
        get() = _formState.value

    init {
        _uiState.value = RoomState.CurrentRoom
    }

    fun onCreatePressed () {
        val model = formState.postModel.value

        if (!formState.isPostValid) {
            _state.value = ResourceState(error = DONT_EMPTY)

            return
        }

        postRoom(room = model)
    }

    fun getUser() {
        userCase.getUser()
            .onEach { res -> onResourceSucceed(res) { usr ->
                val userId: String = usr.id
                formState.onUserIdChange(userId)

                getRooms(userId)
            }}
            .launchIn(viewModelScope)
    }

    fun findRoom(){
        val model = formState.roomId

        if (!formState.isGetValid)
            _state.value = ResourceState(error = DONT_EMPTY)

        getRoom(model)
    }

    override fun postRoom(room: Room) {
        if (room.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        room.apply { createdAt = Date() }

        case.postRoom(room)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getRoom(id: String) {
        if (id.isBlank()){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        case.getRoom(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun patchRoom(id: String, current: Room) {
        if (id.isBlank() || current.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
        }

        current.apply { updatedAt = Date() }

        case.patchRoom(id, current)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun deleteRoom(id: String) {
        if (id.isBlank()){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        case.deleteRoom(id)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getRooms(page: Int, size: Int) {
        if (size == 0){
            _state.value = ResourceState(error = DONT_EMPTY)
        }

        case.getRooms(page, size)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun getRooms(userId: String) {
        if (userId.isBlank())
            _state.value = ResourceState(error = DONT_EMPTY)

        case.getRooms(userId)
            .onEach(this::onResource).launchIn(viewModelScope)
    }

    override fun onBoarding(id: String){
        if (id.isBlank())
            _state.value = ResourceState(error = DONT_EMPTY)

        case.getRoom(id).onEach { resource ->
            onResourceSucceed(resource){ room ->
                val quizzes = room.questions.mapIndexed { index, quiz ->
                    OnBoardingState(
                        quiz = quiz,
                        questionIndex = index +1,
                        totalQuestionsCount = room.questions.size,
                        showPrevious = index > 0,
                        showDone = index == room.questions.size -1
                    )
                }

                if (quizzes.isEmpty()) return@onResourceSucceed

                _uiState.value = RoomState.BoardingQuiz(room, quizzes)
            }
        }.launchIn(viewModelScope)
    }

    override fun computeResult(roomState: RoomState.BoardingQuiz) {
//        val bothId = UUID.randomUUID()
        Log.d(TAG, "computeResult: ${roomState.list.map { it.isCorrect }}")

        /*val result = Result(
            id = "RSL-$bothId",
            roomId = roomState.room.id,
            participantId = "null",
            userId = "null",
            correctQuiz = roomState.list,
            wrongQuiz = "",
            score = "",
            createdAt = "",
            updatedAt = "",
        )*/

        _uiState.value = RoomState.BoardingResult(roomState.room.title, null)
    }

    fun shareResult() {
        TODO("Not yet implemented")
    }

    fun closeResult() {
        _uiState.value = RoomState.CurrentRoom
    }
}