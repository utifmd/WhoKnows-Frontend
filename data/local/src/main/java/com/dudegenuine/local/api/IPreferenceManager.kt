package com.dudegenuine.local.api

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
interface IPreferenceManager {
    fun read(key: String): String
    fun write(key: String, value: String)

    companion object {
        const val PREF_NAME = "WhoKnowsPreference"
        const val CURRENT_USER_ID = "current_user_id"

        const val ONBOARD_ROOM_ID = "onboard_user_id"
        const val ONBOARD_PARTICIPANT_ID = "onboard_participant_id"
    }
}