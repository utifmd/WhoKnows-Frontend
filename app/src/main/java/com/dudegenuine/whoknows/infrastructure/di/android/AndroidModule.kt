package com.dudegenuine.whoknows.infrastructure.di.android

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.room.Room
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.local.api.*
import com.dudegenuine.local.api.IPreferenceManager.Companion.PREF_NAME
import com.dudegenuine.local.manager.WhoKnowsDatabase
import com.dudegenuine.local.manager.contract.IWhoKnowsDatabase.Companion.DATABASE_NAME
import com.dudegenuine.whoknows.infrastructure.di.android.api.*
import com.dudegenuine.whoknows.infrastructure.di.android.contract.IAndroidModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

/**
 * Fri, 03 Dec 2021
 * WhoKnows by utifmd
 **/
@FlowPreview
@ExperimentalCoroutinesApi
@Module
@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@InstallIn(SingletonComponent::class)
object AndroidModule: IAndroidModule {

    @Provides
    @Singleton
    override fun provideLocalDatabase(
        @ApplicationContext context: Context): WhoKnowsDatabase {

        return Room.databaseBuilder(context,
            WhoKnowsDatabase::class.java, DATABASE_NAME).build()
    }

    @Provides
    @Singleton
    override fun provideSharedPreference(
        @ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    override fun provideNotifyManager(
        @ApplicationContext context: Context): INotifyManager =
        NotifyManager(context)

    @Provides
    @Singleton
    override fun providePrefManager(
        preferences: SharedPreferences): IPreferenceManager = PrefsManager(preferences)

    @Provides
    @Singleton
    override fun provideClipboardManager(
        @ApplicationContext context: Context): IClipboardManager {

        return ClipboardManager(context)
    }

    @Provides
    @Singleton
    override fun provideTimerLauncher(
        @ApplicationContext context: Context): ITimerLauncher {

        return TimerModule(context)
    }

    @Provides
    @Singleton
    override fun provideShareModule(
        @ApplicationContext context: Context): IShareLauncher {
        return ShareModule(context)
    }

    @Provides
    @Singleton
    override fun providePrefsFactories(
        prefs: IPreferenceManager): IPrefsFactory {
        return PrefsFactory(prefs)
    }

    @Provides
    @Singleton
    override fun provideBroadcastReceiverModule(
        @ApplicationContext context: Context): IReceiverFactory {
        return ReceiverFactory()
    }
}