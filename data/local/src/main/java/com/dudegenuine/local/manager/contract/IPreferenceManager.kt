package com.dudegenuine.local.manager.contract

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
interface IPreferenceManager {
    fun getString(key: String): String
    fun setString(key: String, value: String)

    companion object {
        const val PREF_NAME = "WhoKnowsPreference"
        const val CURRENT_USER_ID = "current_user_id"
    }
}