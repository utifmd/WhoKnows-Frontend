package com.dudegenuine.whoknows.infrastructure.di.android.api

import android.content.SharedPreferences
import com.dudegenuine.local.api.IPreferenceManager

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
class PrefsManager(
    private val prefs: SharedPreferences): IPreferenceManager { /*private val prefs = context.getSharedPreferences(IPreferenceManager.PREF_NAME, MODE_PRIVATE)*/

    override fun read(key: String): String {
        return prefs.getString(key, "")!!
    }

    override fun write(key: String, value: String) {
        /*
        val editor = prefs.edit()

        editor.putString(key, value)
        editor.apply()*/

        with (prefs.edit()) {
            putString(key, value)
            apply()
        }
    }
}