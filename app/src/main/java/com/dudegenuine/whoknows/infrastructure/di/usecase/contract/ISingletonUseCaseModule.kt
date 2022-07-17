package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.repository.contract.IMessagingRepository
import com.dudegenuine.repository.contract.INotificationRepository
import com.dudegenuine.repository.contract.IRoomRepository

/**
 * Sat, 16 Jul 2022
 * WhoKnows by utifmd
 **/
interface ISingletonUseCaseModule {
    fun provideMessagingSingletonUseCaseModule(
        reposMessaging: IMessagingRepository,
        reposRoom: IRoomRepository,
        reposNotifier: INotificationRepository
    ): ISingleUseCaseModule
}