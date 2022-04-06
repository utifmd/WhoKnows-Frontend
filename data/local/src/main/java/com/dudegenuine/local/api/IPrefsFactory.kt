package com.dudegenuine.local.api

/**
 * Tue, 05 Apr 2022
 * WhoKnows by utifmd
 **/
interface IPrefsFactory {
    var manager: IPreferenceManager
    var userId: String
    var tokenId: String
    var participationId: String
    var notificationBadge: Int
    var runningTime: Int

    companion object {
        const val USER_ID = "FACTORY_USER_ID"
        const val PARTICIPATION_ID = "FACTORY_PARTICIPATION_ID"
        const val TOKEN_ID = "FACTORY_TOKEN_ID"
        const val NOTIFICATION_BADGE = "FACTORY_NOTIFICATION_BADGE"
        const val RUNNING_TIME = "FACTORY_RUNNING_TIME_BOARDING"
    }
}