package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.contract.INotificationRepository
import com.dudegenuine.repository.contract.IRoomRepository
import com.dudegenuine.usecase.messaging.RetrieveMessaging
import com.dudegenuine.usecase.notification.PostNotification
import com.dudegenuine.usecase.participation.DeleteBoarding
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.ISingleUseCaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Sun, 17 Jul 2022
 * WhoKnows by utifmd
 **/
@Module
@InstallIn(SingletonComponent::class)
object SingleUseCaseModule: ISingleUseCaseModule {
    @Provides
    @Singleton
    override fun provideDeleteBoarding(
        reposRoom: IRoomRepository): DeleteBoarding  =
        DeleteBoarding(reposRoom)

    @Provides
    @Singleton
    override fun provideRetrieveMessaging(
        reposNotifier: INotificationRepository): RetrieveMessaging =
        RetrieveMessaging(reposNotifier)

    @Provides
    @Singleton
    override fun providePostNotification(
        reposNotifier: INotificationRepository): PostNotification =
        PostNotification(reposNotifier)
}