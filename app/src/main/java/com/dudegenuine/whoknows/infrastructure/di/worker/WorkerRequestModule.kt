package com.dudegenuine.whoknows.infrastructure.di.worker

import android.content.Context
import com.dudegenuine.repository.contract.dependency.local.ITokenWorkManager
import com.dudegenuine.whoknows.infrastructure.di.android.dependency.TokenWorkManager
import com.dudegenuine.whoknows.infrastructure.di.worker.contract.IWorkerModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Sat, 28 May 2022
 * WhoKnows by utifmd
 **/
@Module
@InstallIn(SingletonComponent::class)
object WorkerRequestModule: IWorkerModule {
    @Provides
    @Singleton
    override fun provideAlarmRoomWorkerRequest(
        @ApplicationContext context: Context): ITokenWorkManager {
        return TokenWorkManager()
    }
}