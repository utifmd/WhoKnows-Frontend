package com.dudegenuine.whoknows.infrastructure.di.android.api

import android.content.SharedPreferences
import com.dudegenuine.local.api.IPreferenceManager

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
class PrefsManager(
    private val prefs: SharedPreferences): IPreferenceManager { /*private val prefs = context.getSharedPreferences(IPreferenceManager.PREF_NAME, MODE_PRIVATE)*/
    override val unregister: (SharedPreferences.OnSharedPreferenceChangeListener) ->
        Unit = { prefs.unregisterOnSharedPreferenceChangeListener(it) }

    override val register: (SharedPreferences.OnSharedPreferenceChangeListener) ->
        Unit = { prefs.registerOnSharedPreferenceChangeListener(it) }

    override fun readString(key: String): String {
        return prefs.getString(key, "")!!
    }

    override fun readInt(key: String): Int {
        return prefs.getInt(key, 0)
    }

    override fun readBoolean(key: String): Boolean {
        return prefs.getBoolean(key, false)
    }

    override fun write(key: String, value: String) {
        with (prefs.edit()) {
            putString(key, value)
            apply()
        }
    }

    override fun write(key: String, value: Int) {
        with (prefs.edit()) {
            putInt(key, value)
            apply()
        }
    }

    override fun write(key: String, value: Boolean) {
        with (prefs.edit()) {
            putBoolean(key, value)
            apply()
        }
    }
}