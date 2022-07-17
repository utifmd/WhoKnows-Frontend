package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.repository.contract.INotificationRepository
import com.dudegenuine.repository.contract.IRoomRepository
import com.dudegenuine.usecase.messaging.RetrieveMessaging
import com.dudegenuine.usecase.notification.PostNotification
import com.dudegenuine.usecase.participation.DeleteBoarding

/**
 * Sat, 16 Jul 2022
 * WhoKnows by utifmd
 **/
interface ISingleUseCaseModule{
    fun provideDeleteBoarding(reposRoom: IRoomRepository): DeleteBoarding
    fun provideRetrieveMessaging(reposNotifier: INotificationRepository): RetrieveMessaging
    fun providePostNotification(reposNotifier: INotificationRepository): PostNotification
}