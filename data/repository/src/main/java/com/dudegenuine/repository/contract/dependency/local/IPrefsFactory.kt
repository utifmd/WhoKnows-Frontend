package com.dudegenuine.repository.contract.dependency.local

/**
 * Tue, 05 Apr 2022
 * WhoKnows by utifmd
 **/
interface IPrefsFactory {
    var manager: IPreferenceManager
    var userId: String
    var tokenId: String
    var participationId: String
    //var notificationBadge: Int
    var runningTime: Int

    /*var createMessaging: String
    var prevOwnedRoomIds: String
    var addMessaging: Boolean
    var removeMessaging: Boolean*/

    companion object {
        const val USER_ID = "FACTORY_USER_ID"
        const val PARTICIPATION_ID = "FACTORY_PARTICIPATION_ID"
        const val TOKEN_ID = "FACTORY_TOKEN_ID"
        const val NOTIFICATION_BADGE = "FACTORY_NOTIFICATION_BADGE"
        const val RUNNING_TIME = "FACTORY_RUNNING_TIME_BOARDING"
        const val CREATE_MESSAGING = "FACTORY_CREATE_MESSAGING"
        const val ADD_MESSAGING = "FACTORY_ADD_MESSAGING"
        const val REMOVE_MESSAGING = "FACTORY_REMOVE_MESSAGING"
        const val PREV_OWNED_ROOM_IDS = "FACTORY_PREV_OWNED_ROOM_IDS"
    }
}