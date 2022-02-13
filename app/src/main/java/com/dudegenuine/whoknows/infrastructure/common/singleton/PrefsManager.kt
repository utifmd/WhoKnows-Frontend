package com.dudegenuine.whoknows.infrastructure.common.singleton

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.dudegenuine.local.api.IPreferenceManager
import com.dudegenuine.local.api.IPreferenceManager.Companion.PREF_NAME

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
class PrefsManager
    private constructor(context: Context): IPreferenceManager {
    private val prefs: SharedPreferences = context
        .getSharedPreferences(PREF_NAME, MODE_PRIVATE)

    companion object: SingletonHolder<PrefsManager, Context>(::PrefsManager)

    override fun read(key: String): String {
        return prefs.getString(key, "")!!
    }

    override fun write(key: String, value: String) {
        with(prefs.edit()) {
            putString(key, value)
            apply()
        }
    }
}