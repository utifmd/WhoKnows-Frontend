package com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event

/**
 * Wed, 26 Jan 2022
 * WhoKnows by utifmd
 **/
interface IRoomEvent {
    fun onNewClassPressed()
    fun onJoinWithACodePressed()
    fun onRoomItemSelected(id: String)

    companion object {
        const val SAVED_KEY_ROOM = "saved_key_room"
    }
}