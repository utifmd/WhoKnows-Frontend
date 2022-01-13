package com.dudegenuine.whoknows.infrastructure.di.service

import android.content.SharedPreferences
import com.dudegenuine.local.database.PreferenceManager
import com.dudegenuine.local.database.contract.IPreferenceManager
import com.dudegenuine.whoknows.infrastructure.di.service.contract.IPrefModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
@Module
@InstallIn(SingletonComponent::class)
class PrefModule: IPrefModule {

    @Provides
    @Singleton
    override fun providePrefManager(
        preferences: SharedPreferences): IPreferenceManager {

        return PreferenceManager(preferences)
    }
}