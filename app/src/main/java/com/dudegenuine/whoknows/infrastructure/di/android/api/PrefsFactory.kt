package com.dudegenuine.whoknows.infrastructure.di.android.api

import com.dudegenuine.local.api.IPreferenceManager
import com.dudegenuine.local.api.IPrefsFactory
import com.dudegenuine.local.api.IPrefsFactory.Companion.ADD_MESSAGING
import com.dudegenuine.local.api.IPrefsFactory.Companion.CREATE_MESSAGING
import com.dudegenuine.local.api.IPrefsFactory.Companion.NOTIFICATION_BADGE
import com.dudegenuine.local.api.IPrefsFactory.Companion.PARTICIPATION_ID
import com.dudegenuine.local.api.IPrefsFactory.Companion.PREV_OWNED_ROOM_IDS
import com.dudegenuine.local.api.IPrefsFactory.Companion.REMOVE_MESSAGING
import com.dudegenuine.local.api.IPrefsFactory.Companion.RUNNING_TIME
import com.dudegenuine.local.api.IPrefsFactory.Companion.TOKEN_ID
import com.dudegenuine.local.api.IPrefsFactory.Companion.USER_ID

/**
 * Tue, 05 Apr 2022
 * WhoKnows by utifmd
 **/
class PrefsFactory(
    override var manager: IPreferenceManager): IPrefsFactory {

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

    override var participationId: String
        get() = manager.readString(PARTICIPATION_ID)
        set(fresh) {
            manager.write(PARTICIPATION_ID, fresh)
        }

    override var notificationBadge: Int
        get() = manager.readInt(NOTIFICATION_BADGE)
        set(fresh) {
            manager.write(NOTIFICATION_BADGE, fresh)
        }

    override var runningTime: Int
        get() = manager.readInt(RUNNING_TIME)
        set(fresh) {
            manager.write(RUNNING_TIME, fresh)
        }

    override var createMessaging: String
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
        }
}