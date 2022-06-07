package com.dudegenuine.repository.contract.dependency.local

import android.content.SharedPreferences

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
interface IPreferenceManager {
    fun readString(key: String): String
    fun readInt(key: String): Int
    fun readBoolean(key: String): Boolean
    fun write(key: String, value: String)
    fun write(key: String, value: Int)
    fun write(key: String, value: Boolean)
    val unregister: (listener: SharedPreferences.OnSharedPreferenceChangeListener) -> Unit
    val register: (listener: SharedPreferences.OnSharedPreferenceChangeListener) -> Unit

    companion object {
        const val PREF_NAME = "WhoKnowsPreference"
        const val CURRENT_USER_ID = "current_user_id"
        const val CURRENT_NOTIFICATION_BADGE = "current_notification_badge"
        const val CURRENT_NOTIFICATION_BADGE_STATUS = "current_notification_badge_status"

        const val ONBOARD_ROOM_ID = "onboard_user_id"
        const val ONBOARD_PARTICIPANT_ID = "onboard_participant_id"
    }
}