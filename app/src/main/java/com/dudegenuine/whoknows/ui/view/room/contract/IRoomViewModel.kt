package com.dudegenuine.whoknows.ui.view.room.contract

import com.dudegenuine.model.Room

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IRoomViewModel {
    fun postRoom(user: Room)
    fun getRoom(id: String)
    fun patchRoom(id: String, current: Room)
    fun deleteRoom(id: String)
    fun getRooms(page: Int, size: Int)
}