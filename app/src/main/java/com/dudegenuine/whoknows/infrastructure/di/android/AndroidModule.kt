package com.dudegenuine.whoknows.infrastructure.di.android

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.dudegenuine.local.manager.IWhoKnowsDatabase
import com.dudegenuine.local.manager.contract.IWhoKnowsDatabase.Companion.DATABASE_NAME
import com.dudegenuine.repository.contract.dependency.local.*
import com.dudegenuine.repository.contract.dependency.local.IPreferenceManager.Companion.PREF_NAME
import com.dudegenuine.repository.contract.dependency.remote.IFirebaseManager
import com.dudegenuine.whoknows.infrastructure.di.android.contract.IAndroidModule
import com.dudegenuine.whoknows.infrastructure.di.android.dependency.*
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
    override fun provideWorkManager(
        @ApplicationContext context: Context): IWorkerManager {
        return WorkerManager(context)
    }

    @Provides
    @Singleton
    override fun provideAlarmManager(
        @ApplicationContext context: Context): IAlarmManager {

        return AlarmManager(context)
    }

    @Provides
    @Singleton
    override fun provideFirebaseManager(
        @ApplicationContext context: Context): IFirebaseManager {
        return FirebaseManager()
    }

    @Provides
    @Singleton
    override fun provideLocalDatabase(
        @ApplicationContext context: Context): IWhoKnowsDatabase {

        return Room.databaseBuilder(context,
            IWhoKnowsDatabase::class.java, DATABASE_NAME).build()
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
        prefs: IPreferenceManager
    ): IPrefsFactory {
        return PrefsFactory(prefs)
    }

    @Provides
    @Singleton
    override fun provideIntentFactories(
        @ApplicationContext context: Context): IIntentFactory {
        return IntentFactory(context)
    }

    @Provides
    @Singleton
    override fun provideBroadcastReceiverModule(
        @ApplicationContext context: Context): IReceiverFactory {
        return ReceiverFactory()
    }

    @Provides
    @Singleton
    override fun provideResourceDependency(
        @ApplicationContext context: Context): IResourceDependency {
        return ResourceDependency(context)
    }
}