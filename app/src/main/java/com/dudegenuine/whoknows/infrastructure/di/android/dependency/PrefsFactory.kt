package com.dudegenuine.whoknows.infrastructure.di.android.dependency

import com.dudegenuine.repository.contract.dependency.local.IPreferenceManager
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory.Companion.ROOM_ALARM_ID
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory.Companion.PARTICIPANT_TIME_LEFT
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory.Companion.TOKEN_ID
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory.Companion.USER_ID

/**
 * Tue, 05 Apr 2022
 * WhoKnows by utifmd
 **/
class PrefsFactory(
    override var manager: IPreferenceManager
): IPrefsFactory {

    override var userId: String
        get() = manager.readString(USER_ID)
        set(fresh) {
            manager.write(USER_ID, fresh)
        }

    override var tokenId: String
        get() = manager.readString(TOKEN_ID)
        set(fresh) {
            manager.write(TOKEN_ID, fresh)
        }

    /*override var participationId: String
        get() = manager.readString(PARTICIPATION_ID)
        set(fresh) {
            manager.write(PARTICIPATION_ID, fresh)
        }*/

    override var roomAlarm: Boolean
        get() = manager.readBoolean(ROOM_ALARM_ID)
        set(value) {
            manager.write(ROOM_ALARM_ID, value)
        }

    /*override var notificationBadge: Int
        get() = manager.readInt(NOTIFICATION_BADGE)
        set(fresh) {
            manager.write(NOTIFICATION_BADGE, fresh)
        }*/

    override var participantTimeLeft: Int
        get() = manager.readInt(PARTICIPANT_TIME_LEFT)
        set(fresh) {
            manager.write(PARTICIPANT_TIME_LEFT, fresh)
        }

    /*override var createMessaging: String
        get() = manager.readString(CREATE_MESSAGING)
        set(fresh) {
            manager.write(CREATE_MESSAGING, fresh)
        }

    override var prevOwnedRoomIds: String
        get() = manager.readString(PREV_OWNED_ROOM_IDS)
        set(fresh) {
            manager.write(PREV_OWNED_ROOM_IDS, fresh)
        }

    override var addMessaging: Boolean
        get() = manager.readBoolean(ADD_MESSAGING)
        set(fresh) {
            manager.write(ADD_MESSAGING, fresh)
        }

    override var removeMessaging: Boolean
        get() = manager.readBoolean(REMOVE_MESSAGING)
        set(fresh) {
            manager.write(REMOVE_MESSAGING, fresh)
        }*/
}