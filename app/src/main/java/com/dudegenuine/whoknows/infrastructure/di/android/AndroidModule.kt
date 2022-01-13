package com.dudegenuine.whoknows.infrastructure.di.android

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.dudegenuine.local.database.WhoKnowsDatabase
import com.dudegenuine.local.database.contract.IPreferenceManager.Companion.PREF_NAME
import com.dudegenuine.local.database.contract.IWhoKnowsDatabase.Companion.DATABASE_NAME
import com.dudegenuine.whoknows.infrastructure.di.android.contract.IAndroidModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Fri, 03 Dec 2021
 * WhoKnows by utifmd
 **/
@Module
@InstallIn(SingletonComponent::class)
object AndroidModule: IAndroidModule {

    @Provides
    @Singleton
    override fun provideLocalDatabase(
        @ApplicationContext context: Context): WhoKnowsDatabase =

        Room.databaseBuilder(context, WhoKnowsDatabase::class.java, DATABASE_NAME).build()

    @Provides
    @Singleton
    override fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    //    @Provides
//    @Singleton
//    override fun provideSavedStateHandleModule(): SavedStateHandle {
//        return SavedStateHandle()
//    }
}