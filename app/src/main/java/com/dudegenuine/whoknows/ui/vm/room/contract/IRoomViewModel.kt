package com.dudegenuine.whoknows.ui.vm.room.contract

import com.dudegenuine.model.Room

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

    /*fun onBoarding(id: String)*/
    /*fun onBoarding(roomId: String, participantId: String)
    fun computeResult(boardingState: RoomState.BoardingQuiz)*/
}