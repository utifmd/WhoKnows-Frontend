package com.dudegenuine.whoknows.infrastructure.di.android.dependency

import com.dudegenuine.repository.contract.dependency.local.IPreferenceManager
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory.Companion.PARTICIPANT_PPN_ID
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory.Companion.PARTICIPANT_ROOM_ID
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory.Companion.PARTICIPANT_TIME_LEFT
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory.Companion.ROOM_ALARM_ID
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory.Companion.TOKEN_ID
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory.Companion.USER_ID

/**
 * Tue, 05 Apr 2022
 * WhoKnows by utifmd
 **/
class PrefsFactory(
    override var manager: IPreferenceManager): IPrefsFactory {

    override var userId: String
        get() = manager.readString(USER_ID)
        set(fresh) = manager.write(USER_ID, fresh)

    override var tokenId: String
        get() = manager.readString(TOKEN_ID)
        set(fresh) = manager.write(TOKEN_ID, fresh)

    override var roomAlarm: Boolean
        get() = manager.readBoolean(ROOM_ALARM_ID)
        set(value) = manager.write(ROOM_ALARM_ID, value)

    override var participationTimeLeft: Int
        get() = manager.readInt(PARTICIPANT_TIME_LEFT)
        set(fresh) = manager.write(PARTICIPANT_TIME_LEFT, fresh)

    override var participationRoomId: String
        get() = manager.readString(PARTICIPANT_ROOM_ID)
        set(value) = manager.write(PARTICIPANT_ROOM_ID, value)

    override var participationParticipantId: String
        get() = manager.readString(PARTICIPANT_PPN_ID)
        set(value) = manager.write(PARTICIPANT_PPN_ID, value)
}