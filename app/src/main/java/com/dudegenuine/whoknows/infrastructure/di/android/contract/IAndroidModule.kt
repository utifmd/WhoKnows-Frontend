package com.dudegenuine.whoknows.infrastructure.di.android.contract

import android.content.Context
import android.content.SharedPreferences
import com.dudegenuine.local.manager.IWhoKnowsDatabase
import com.dudegenuine.repository.contract.dependency.local.*
import com.dudegenuine.repository.contract.dependency.remote.IFirebaseManager

/**
 * Fri, 03 Dec 2021
 * WhoKnows by utifmd
 **/
interface IAndroidModule {
    fun provideWorkManager(context: Context): IWorkerManager
    fun provideAlarmManager(context: Context): IAlarmManager
    fun provideFirebaseManager(context: Context): IFirebaseManager
    fun provideLocalDatabase(context: Context): IWhoKnowsDatabase
    fun provideSharedPreference(context: Context): SharedPreferences
    fun provideNotifyManager(context: Context): INotifyManager
    fun providePrefManager(preferences: SharedPreferences): IPreferenceManager
    fun provideClipboardManager(context: Context): IClipboardManager
    fun provideTimerLauncher(context: Context): ITimerLauncher
    fun provideShareModule(context: Context): IShareLauncher
    fun providePrefsFactories(prefs: IPreferenceManager): IPrefsFactory
    fun provideIntentFactories(context: Context): IIntentFactory
    fun provideBroadcastReceiverModule(context: Context): IReceiverFactory
    fun provideResourceDependency(context: Context): IResourceDependency
}