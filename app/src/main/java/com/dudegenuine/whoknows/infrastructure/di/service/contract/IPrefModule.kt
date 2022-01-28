package com.dudegenuine.whoknows.infrastructure.di.service.contract

import android.content.Context
import com.dudegenuine.local.database.contract.IPreferenceManager

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
interface IPrefModule {
    /*fun providePrefManager(preferences: SharedPreferences): IPreferenceManager*/
    fun providePrefManager(context: Context): IPreferenceManager
}