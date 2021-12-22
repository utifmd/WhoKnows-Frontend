package com.dudegenuine.whoknows.ui.presenter.room

import android.util.Log
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.Room
import com.dudegenuine.usecase.room.*
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

    val state: State<ViewState> = _state

    init {
        Log.d(TAG, "initial: triggered")
        // getRoom("ROOM00001")
        getRooms(0, 10)
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