package com.dudegenuine.whoknows.infrastructure.di.usecase

import android.content.BroadcastReceiver
import com.dudegenuine.repository.contract.IMessagingRepository
import com.dudegenuine.usecase.messaging.*
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

    override val removeMessaging: RemoveMessaging =
        RemoveMessaging(repository)
    override val onInternetReceived: (onConnected: (String) -> Unit) ->
        BroadcastReceiver = repository.onNetworkReceived

    override val onTokenReceived: (onTokenized: (String) -> Unit) ->
        BroadcastReceiver = repository.onTokenReceived

}