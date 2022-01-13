package com.dudegenuine.whoknows.ui.presenter.room

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.Resource
import com.dudegenuine.model.Room
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IRoomUseCaseModule
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
    private val savedStateHandle: SavedStateHandle): BaseViewModel(), IRoomViewModel {

    private val TAG: String = javaClass.simpleName

    val resourceState: State<ResourceState>
        get() = _state

    private val _uiState = MutableLiveData<RoomState>()
    val uiState: LiveData<RoomState>
        get() = _uiState

    private val _createState = mutableStateOf(RoomState.CreateRoom())
    val createState: RoomState.CreateRoom
        get() = _createState.value

    init {
        //_uiState.value = RoomState.CreateRoom()

        _uiState.value = RoomState.CurrentRoom

        getRooms("USR-0001")
    }

    fun onCreatePressed () {
        Log.d(TAG, "onCreatePressed: triggered") //Log.d(TAG, createState.model.toString())
        val model = createState.model.value

        if (!createState.isValid.value) {
            _state.value = ResourceState(error = DONT_EMPTY)

            return
        }

        postRoom(room = model)
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
        case.getRoom(id).onEach { resource ->
            when(resource){
                is Resource.Success -> {
                    resource.data?.let { room ->
                        val quizzes = room.questions.mapIndexed { index, quiz ->
                            OnBoardingState(
                                quiz = quiz,
                                questionIndex = index +1,
                                totalQuestionsCount = room.questions.size,
                                showPrevious = index > 0,
                                showDone = index == room.questions.size -1
                            )
                        }

                        if (quizzes.isEmpty()) return@let

                        _uiState.value = RoomState.BoardingQuiz(room, quizzes)
                    }
                }
                is Resource.Error -> _state.value = ResourceState(
                    error = resource.message ?: "An unexpected error occurred.")
                is Resource.Loading -> _state.value = ResourceState(
                    loading = true)
            }
        }.launchIn(viewModelScope)
    }

    override fun computeResult(roomState: RoomState.BoardingQuiz) {
//        val bothId = UUID.randomUUID()
        Log.d(TAG, "computeResult: ${roomState.list.map { it.isCorrect }}")

//        val result = Result(
//            id = "RSL-$bothId",
//            roomId = roomState.room.id,
//            participantId = "null",
//            userId = "null",
//            correctQuiz = roomState.list,
//            wrongQuiz = "",
//            score = "",
//            createdAt = "",
//            updatedAt = "",
//        )

        _uiState.value = RoomState.BoardingResult(roomState.room.title, null)
    }

    fun shareResult() {
        TODO("Not yet implemented")
    }

    fun closeResult() {
        _uiState.value = RoomState.CurrentRoom
    }
}