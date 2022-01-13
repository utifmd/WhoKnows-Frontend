package com.dudegenuine.whoknows.infrastructure.di.android.contract

import android.content.Context
import android.content.SharedPreferences
import com.dudegenuine.local.database.WhoKnowsDatabase

/**
 * Fri, 03 Dec 2021
 * WhoKnows by utifmd
 **/
interface IAndroidModule {
    fun provideLocalDatabase(context: Context): WhoKnowsDatabase
    fun provideSharedPreference(context: Context): SharedPreferences
    //fun provideSavedStateHandleModule(): SavedStateHandle
}