package com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event

import com.dudegenuine.model.Participant
import com.dudegenuine.model.Quiz
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

        /*const val ONBOARD_PPN_ID_SAVED_KEY = "onboard_room_participant_id"*/
        const val ONBOARD_ROOM_ID_SAVED_KEY = "onboard_room_id"
    }
}

interface IRoomEventHome: IRoomEvent {
    fun onNewClassPressed()
    fun onJoinRoomWithACodePressed()
    fun onRoomItemSelected(id: String)
}

interface IRoomEventDetail: IRoomEvent {
    fun onBackPressed(){}
    fun onParticipantLongPressed(enabled: Boolean, participant: Participant){}
    fun onQuestionLongPressed(room: Room.Complete, quiz: Quiz.Complete){}//(enabled: Boolean, quiz: Quiz.Complete, roomId: String){}
    //fun onRoomDetailPressed(roomId: String){}
    fun onCloseRoomPressed(room: Room.Complete, finished: () -> Unit){}
    fun onShareRoomPressed(room: Room.Complete){}
    fun onDeleteRoomPressed(room: Room.Complete){}
    fun onDeleteRoomSucceed(){}
    fun onNewRoomQuizPressed(roomId: String, owner: String){}
    fun onJoinRoomDirectlyPressed(room: Room.Complete/*roomId: String*/){}
    fun onBoardingRoomPressed(roomId: String){}
    fun onParticipantItemPressed(userId: String){}
    fun onQuestionItemPressed(quizId: String){}
    fun onResultPressed(roomId: String, userId: String){}
}

interface IRoomEventBoarding: IRoomEvent {
    fun onAction(index: Int, type: Quiz.Action.Type) {}
    fun onBackPressed() {}
    fun onPrevPressed() {}
    fun onNextPressed() {}
    fun onDonePressed() {}

    fun onDoneResultPressed() {}
}