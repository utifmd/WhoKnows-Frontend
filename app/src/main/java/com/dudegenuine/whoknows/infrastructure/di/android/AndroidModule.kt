package com.dudegenuine.whoknows.infrastructure.di.android

import com.dudegenuine.whoknows.infrastructure.di.android.contract.IAndroidModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Fri, 03 Dec 2021
 * WhoKnows by utifmd
 **/
@Module
@InstallIn(SingletonComponent::class)
object AndroidModule: IAndroidModule {

//    @Provides
//    @Singleton
//    override fun provideSavedStateHandleModule(): SavedStateHandle {
//        return SavedStateHandle()
//    }
}