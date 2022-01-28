package com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event

import com.dudegenuine.model.Room

/**
 * Wed, 26 Jan 2022
 * WhoKnows by utifmd
 **/
interface IRoomEvent {
    companion object {
        const val SAVED_KEY_ROOM = "saved_key_room"
    }
}

interface IRoomEventHome: IRoomEvent {
    fun onNewClassPressed()
    fun onJoinRoomWithACodePressed()
    fun onRoomItemSelected(id: String)
}

interface IRoomEventDetail: IRoomEvent {
    fun onPublishRoomPressed(){}
    fun onNewRoomQuizPressed(){}
    fun onJoinRoomDirectlyPressed(room: Room){}
    fun onParticipantItemPressed(){}
}
