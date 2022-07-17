package com.dudegenuine.repository.contract.dependency.local

/**
 * Tue, 05 Apr 2022
 * WhoKnows by utifmd
 **/
interface IPrefsFactory {
    var manager: IPreferenceManager
    var userId: String
    var tokenId: String
    //var participationId: String
    var roomAlarm: Boolean
    //var notificationBadge: Int
    var participationRoomId: String
    var participationParticipantId: String
    var participationTimeLeft: Int

    /*var createMessaging: String
    var prevOwnedRoomIds: String
    var addMessaging: Boolean
    var removeMessaging: Boolean*/

    companion object {
        const val USER_ID = "FACTORY_USER_ID"
        const val PARTICIPATION_ID = "FACTORY_PARTICIPATION_ID"
        const val ROOM_ALARM_ID = "FACTORY_ROOM_ALARM_ID"
        const val TOKEN_ID = "FACTORY_TOKEN_ID"
        const val NOTIFICATION_BADGE = "FACTORY_NOTIFICATION_BADGE"
        const val PARTICIPANT_TIME_LEFT = "FACTORY_PARTICIPANT_TIME_LEFT"
        const val PARTICIPANT_ROOM_ID = "FACTORY_PARTICIPANT_ROOM_ID"
        const val PARTICIPANT_PPN_ID = "FACTORY_PARTICIPANT_PPN_ID"
        const val CREATE_MESSAGING = "FACTORY_CREATE_MESSAGING"
        const val ADD_MESSAGING = "FACTORY_ADD_MESSAGING"
        const val REMOVE_MESSAGING = "FACTORY_REMOVE_MESSAGING"
        const val PREV_OWNED_ROOM_IDS = "FACTORY_PREV_OWNED_ROOM_IDS"
    }
}