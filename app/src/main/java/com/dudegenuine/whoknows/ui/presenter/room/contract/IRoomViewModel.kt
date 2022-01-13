package com.dudegenuine.whoknows.ui.presenter.room.contract

import com.dudegenuine.model.Room
import com.dudegenuine.whoknows.ui.compose.state.RoomState

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IRoomViewModel {
    fun postRoom(room: Room)
    fun getRoom(id: String)
    fun patchRoom(id: String, current: Room)
    fun deleteRoom(id: String)
    fun getRooms(page: Int, size: Int)
    fun getRooms(userId: String)

    fun onBoarding(id: String)
    fun computeResult(roomState: RoomState.BoardingQuiz)
}