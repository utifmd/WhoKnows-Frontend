package com.dudegenuine.whoknows.infrastructure.di.service.contract

import android.content.Context
import android.content.SharedPreferences
import com.dudegenuine.local.manager.contract.IClipboardManager
import com.dudegenuine.local.manager.contract.IPreferenceManager

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
interface ILocalManagerModule {
    fun providePrefManager(preferences: SharedPreferences): IPreferenceManager
    /*fun providePrefManager(context: Context): IPreferenceManager*/
    fun provideClipboardManager(context: Context): IClipboardManager
}