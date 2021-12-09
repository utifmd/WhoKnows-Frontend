package com.dudegenuine.whoknows.ui.view.room

import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.Room
import com.dudegenuine.usecase.room.*
import com.dudegenuine.whoknows.ui.view.BaseViewModel
import com.dudegenuine.whoknows.ui.view.ViewState
import com.dudegenuine.whoknows.ui.view.room.contract.IRoomViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        // getRoom("ROOM00001")
        getRooms(0, 10)
    }

    override fun postRoom(user: Room) {
        postRoomUseCase(user)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getRoom(id: String) {
        getRoomUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun patchRoom(id: String, current: Room) {
        patchRoomUseCase(id, current)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun deleteRoom(id: String) {
        deleteRoomUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getRooms(page: Int, size: Int) {
        getRoomsUseCase(page, size)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }
}