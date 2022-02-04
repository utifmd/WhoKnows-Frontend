package com.dudegenuine.whoknows.infrastructure.di.android.contract

import android.content.Context
import android.content.SharedPreferences
import com.dudegenuine.local.manager.WhoKnowsDatabase
import com.dudegenuine.local.manager.contract.IBroadcastReceiverManager
import com.dudegenuine.whoknows.infrastructure.di.service.contract.ILocalManagerModule

/**
 * Fri, 03 Dec 2021
 * WhoKnows by utifmd
 **/
interface IAndroidModule: ILocalManagerModule {
    fun provideLocalDatabase(context: Context): WhoKnowsDatabase
    fun provideSharedPreference(context: Context): SharedPreferences
    fun provideBroadcastReceiverManager(context: Context): IBroadcastReceiverManager
    //fun provideSavedStateHandleModule(): SavedStateHandle
}