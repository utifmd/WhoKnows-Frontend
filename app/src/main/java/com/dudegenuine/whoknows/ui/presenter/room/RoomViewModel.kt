package com.dudegenuine.whoknows.ui.presenter.room

import androidx.compose.runtime.State
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.Room
import com.dudegenuine.usecase.room.*
import com.dudegenuine.whoknows.ui.compose.state.OnBoardingState
import com.dudegenuine.whoknows.ui.compose.state.RoomState
import com.dudegenuine.whoknows.ui.presenter.BaseViewModel
import com.dudegenuine.whoknows.ui.presenter.ViewState
import com.dudegenuine.whoknows.ui.presenter.ViewState.Companion.DONT_EMPTY
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
    private val postRoomUseCase: PostRoom,
    private val getRoomUseCase: GetRoom,
    private val patchRoomUseCase: PatchRoom,
    private val deleteRoomUseCase: DeleteRoom,
    private val getRoomsUseCase: GetRooms): BaseViewModel(), IRoomViewModel {
    private val TAG: String = javaClass.simpleName

    val resourceState: State<ViewState>
        get() = _state

    private val _uiState = MutableLiveData<RoomState>()
    val uiState: LiveData<RoomState>
        get() = _uiState

    private lateinit var roomInitialState: RoomState

    init {
        /*
        * Boarding
        * */ // Log.d(TAG, "initial: triggered") // getRooms(0, 10)
        getRoom("ROM-f80365e5-0e65-4674-9e7b-bee666b62bda")

        resourceState.value.room?.let { room ->
            roomInitialState = RoomState.BoardingQuiz(room)
            _uiState.value = roomInitialState

            room.questions?.let { quizzes ->
                quizzes.mapIndexed { index, quiz ->
                    OnBoardingState(
                        quiz = quiz,
                        questionIndex = index,
                        totalQuestionsCount = quizzes.size,
                        showPrevious = index > 0,
                        showDone = index == quizzes.size -1
                    )
                }
            }
        }
    }

    override fun postRoom(room: Room) {
        if (room.isPropsBlank){
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        room.apply { createdAt = Date() }

        postRoomUseCase(room)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getRoom(id: String) {
        if (id.isBlank()){
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        getRoomUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun patchRoom(id: String, current: Room) {
        if (id.isBlank() || current.isPropsBlank){
            _state.value = ViewState(error = DONT_EMPTY)
        }

        current.apply { updatedAt = Date() }

        patchRoomUseCase(id, current)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun deleteRoom(id: String) {
        if (id.isBlank()){
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        deleteRoomUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getRooms(page: Int, size: Int) {
        if (size == 0){
            _state.value = ViewState(error = DONT_EMPTY)
        }

        getRoomsUseCase(page, size)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }
}