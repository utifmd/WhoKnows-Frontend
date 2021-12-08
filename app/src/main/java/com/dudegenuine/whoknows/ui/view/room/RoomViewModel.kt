package com.dudegenuine.whoknows.ui.view.room

import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.Room
import com.dudegenuine.usecase.room.GetRoom
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
    private val getRoomUseCase: GetRoom ): BaseViewModel(), IRoomViewModel {
    private val TAG: String = javaClass.simpleName

    val state: State<ViewState> = _state

    init {
        getRoom("ROOM00001")
    }

    override fun postRoom(user: Room) {

    }

    override fun getRoom(id: String) {
        getRoomUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun patchRoom(id: String, current: Room) {

    }

    override fun deleteRoom(id: String) {

    }

    override fun getRooms(page: Int, size: Int) {

    }
}