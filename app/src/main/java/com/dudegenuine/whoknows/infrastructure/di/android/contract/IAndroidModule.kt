package com.dudegenuine.whoknows.infrastructure.di.android.contract

import android.content.Context
import android.content.SharedPreferences
import com.dudegenuine.local.api.IClipboardManager
import com.dudegenuine.local.api.INotifyManager
import com.dudegenuine.local.api.IPreferenceManager
import com.dudegenuine.local.manager.WhoKnowsDatabase

/**
 * Fri, 03 Dec 2021
 * WhoKnows by utifmd
 **/
interface IAndroidModule {
    fun provideLocalDatabase(context: Context): WhoKnowsDatabase
    fun provideSharedPreference(context: Context): SharedPreferences
    fun provideNotifyManager(context: Context): INotifyManager
    fun providePrefManager(preferences: SharedPreferences): IPreferenceManager
    fun provideClipboardManager(context: Context): IClipboardManager
}