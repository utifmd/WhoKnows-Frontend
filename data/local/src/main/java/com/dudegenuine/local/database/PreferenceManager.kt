package com.dudegenuine.local.database

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.dudegenuine.local.database.contract.IPreferenceManager

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
class PreferenceManager(
    context: Context): IPreferenceManager {

    private val prefs = context.getSharedPreferences(IPreferenceManager.PREF_NAME, MODE_PRIVATE)

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