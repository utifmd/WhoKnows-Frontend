package com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event

import com.dudegenuine.model.Room

/**
 * Wed, 26 Jan 2022
 * WhoKnows by utifmd
 **/
interface IRoomEvent {
    companion object {
        const val ROOM_ID_SAVED_KEY = "room_id_saved_key"
        const val ROOM_OWNER_SAVED_KEY = "room_owner_saved_key"
        const val ROOM_IS_OWN = "room_is_own"

        const val OWN_IS_TRUE = "true"
        const val OWN_IS_FALSE = "false"
    }
}

interface IRoomEventHome: IRoomEvent {
    fun onNewClassPressed()
    fun onJoinRoomWithACodePressed()
    fun onRoomItemSelected(id: String)
}

interface IRoomEventDetail: IRoomEvent {
    fun onPublishRoomPressed(){}
    fun onCloseRoomPressed(room: Room){}
    fun onShareRoomPressed(roomId: String){}
    fun onDeleteRoomPressed(roomId: String){}
    fun onDeleteRoomSucceed(roomId: String){}
    fun onNewRoomQuizPressed(roomId: String, owner: String){}
    fun onJoinRoomDirectlyPressed(room: Room){}
    fun onParticipantItemPressed(userId: String){}
    fun onQuestionItemPressed(quizId: String){}
}
