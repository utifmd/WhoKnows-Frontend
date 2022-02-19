package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.usecase.messaging.AddMessaging
import com.dudegenuine.usecase.messaging.CreateMessaging
import com.dudegenuine.usecase.messaging.GetMessaging
import com.dudegenuine.usecase.messaging.PushMessaging

/**
 * Mon, 14 Feb 2022
 * WhoKnows by utifmd
 **/
interface IMessageUseCaseModule {
    val pushMessaging: PushMessaging
    val getMessaging: GetMessaging
    val createMessaging: CreateMessaging
    val addMessaging: AddMessaging

    val onMessagingTokenized: () -> String
    val onMessagingTokenRefresh: (String) -> Unit
}