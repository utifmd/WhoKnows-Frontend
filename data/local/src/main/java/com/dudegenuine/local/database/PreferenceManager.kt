package com.dudegenuine.local.database

import android.content.SharedPreferences
import com.dudegenuine.local.database.contract.IPreferenceManager

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
class PreferenceManager(
    private val sharedPreferences: SharedPreferences): IPreferenceManager {

    override fun getString(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }

    override fun setString(key: String, value: String) {
        sharedPreferences.edit().apply {
            putString(key, value)
            apply()
        }
    }
}