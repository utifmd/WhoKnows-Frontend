package com.dudegenuine.local.manager

import android.content.SharedPreferences
import com.dudegenuine.local.manager.contract.IPreferenceManager

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
class PreferenceManager(
    private val prefs: SharedPreferences): IPreferenceManager {

    /*private val prefs = context.getSharedPreferences(IPreferenceManager.PREF_NAME, MODE_PRIVATE)*/

    override fun getString(key: String): String {
        return prefs.getString(key, "")!!
    }

    override fun setString(key: String, value: String) {
        val editor = prefs.edit()

        editor.putString(key, value)
        editor.apply()

        /*sharedPreferences.edit().apply {
            putString(key, value)
            apply()
        }*/
    }
}