package com.dudegenuine.whoknows.ux.compose.state.room

import com.dudegenuine.model.Room

/**
 * Fri, 10 Jun 2022
 * WhoKnows by utifmd
 **/
sealed class FlowParameter{
    //data class UserId(val item: String): FlowParameter()

    object Nothing: FlowParameter()
    data class RoomComplete(
        val userId: String, val list: List<Room.Complete> = emptyList()): FlowParameter()
    data class Notification(val userId: String): FlowParameter()
}