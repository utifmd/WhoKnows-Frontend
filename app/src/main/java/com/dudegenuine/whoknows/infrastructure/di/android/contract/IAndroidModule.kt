package com.dudegenuine.whoknows.infrastructure.di.android.contract

import android.content.Context
import android.content.SharedPreferences
import com.dudegenuine.local.api.ILocalStoreModule
import com.dudegenuine.local.manager.WhoKnowsDatabase

/**
 * Fri, 03 Dec 2021
 * WhoKnows by utifmd
 **/
interface IAndroidModule: ILocalStoreModule {
    fun provideLocalDatabase(context: Context): WhoKnowsDatabase
    fun provideSharedPreference(context: Context): SharedPreferences
    /*fun provideNotificationManager(context: Context): NotificationManager
    fun provideNotificationService(context: Context, manager: NotificationManager): INotificationService*/
}