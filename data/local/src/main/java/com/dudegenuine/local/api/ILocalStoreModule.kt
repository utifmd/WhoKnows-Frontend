package com.dudegenuine.local.api

import android.content.Context
import android.content.SharedPreferences

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
interface ILocalStoreModule {
    fun providePrefManager(preferences: SharedPreferences): IPreferenceManager
    /*fun providePrefManager(context: Context): IPreferenceManager*/
    fun provideClipboardManager(context: Context): IClipboardManager
}