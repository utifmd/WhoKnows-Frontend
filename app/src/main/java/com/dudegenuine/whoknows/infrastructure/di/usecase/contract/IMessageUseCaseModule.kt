package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory
import com.dudegenuine.repository.contract.dependency.local.IReceiverFactory
import com.dudegenuine.repository.contract.dependency.local.IWorkerManager
import com.dudegenuine.repository.contract.dependency.remote.IFirebaseManager
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

    val receiver: IReceiverFactory
    val preference: IPrefsFactory
    val firebase: IFirebaseManager
    val workerManager: IWorkerManager
}