package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.usecase.messaging.*

/**
 * Mon, 14 Feb 2022
 * WhoKnows by utifmd
 **/
interface IMessageUseCaseModule {
    val pushMessaging: PushMessaging
    val getMessaging: GetMessaging
    val createMessaging: CreateMessaging
    val addMessaging: AddMessaging
    val removeMessaging: RemoveMessaging

    val onMessagingTokenized: () -> String
    val onMessagingTokenRefresh: (String) -> Unit
}