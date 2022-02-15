package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.contract.IMessagingRepository
import com.dudegenuine.usecase.messaging.AddMessaging
import com.dudegenuine.usecase.messaging.CreateMessaging
import com.dudegenuine.usecase.messaging.GetMessaging
import com.dudegenuine.usecase.messaging.PushMessaging
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IMessageUseCaseModule

/**
 * Mon, 14 Feb 2022
 * WhoKnows by utifmd
 **/
class MessageUseCaseModule(repository: IMessagingRepository): IMessageUseCaseModule {

    override val pushMessaging: PushMessaging =
        PushMessaging(repository)

    override val getMessaging: GetMessaging =
        GetMessaging(repository)

    override val createMessaging: CreateMessaging =
        CreateMessaging(repository)

    override val addMessaging: AddMessaging =
        AddMessaging(repository)
}