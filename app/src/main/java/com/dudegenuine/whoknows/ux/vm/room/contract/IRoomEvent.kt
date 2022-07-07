package com.dudegenuine.whoknows.ux.vm.room.contract

import com.dudegenuine.model.Participant
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.Room

/**
 * Wed, 26 Jan 2022
 * WhoKnows by utifmd
 **/
interface IRoomEvent {
    fun onBackPressed()

    companion object {
        const val ROOM_ID_SAVED_KEY = "room_id_saved_key"
        const val ROOM_OWNER_SAVED_KEY = "room_owner_saved_key"
        const val ROOM_IS_OWN = "room_is_own"

        const val OWN_IS_TRUE = "true"
        const val OWN_IS_FALSE = "false"

        /*const val ONBOARD_PPN_ID_SAVED_KEY = "onboard_room_participant_id"
        const val KEY_PARTICIPATION_ROOM_ID = "KEY_PARTICIPATION_ROOM_ID"
        * */
    }
}

interface IRoomEventHome: IRoomEvent {
    fun onNewClassPressed()
    fun onButtonJoinRoomWithACodePressed()
    fun onRoomHomeScreenDetailSelected(id: String)
    fun onNotificationPressed()
    //fun onImpressed()
}

interface IRoomEventDetail: IRoomEvent {
    fun onBackRoomDetailPressed()
    fun onParticipantLongPressed(enabled: Boolean, participant: Participant, setIsRefresh: (Boolean) -> Unit)
    fun onQuestionLongPressed(room: Room.Complete, quiz: Quiz.Complete, setIsRefresh: (Boolean) -> Unit)
    fun onCloseRoomPressed(room: Room.Complete, onComplete: () -> Unit)
    fun onShareRoomPressed(room: Room.Complete)
    fun onSetCopyRoomPressed(room: Room.Complete)
    fun onDeleteRoomPressed(room: Room.Complete)
    fun onNewRoomQuizPressed(room: Room.Complete)
    fun onJoinButtonRoomDetailPressed(room: Room.Complete)
    fun onParticipationDecided(roomId: String)
    fun onParticipantItemPressed(userId: String)
    fun onQuestionItemPressed(quizId: String)
    fun onResultPressed(roomId: String, userId: String)
    fun onReNavigateRoom(roomId: String)
    /*fun turnOnAlarm(minute: Int)
    fun turnOffAlarm()*/
}