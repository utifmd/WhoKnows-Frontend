package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.contract.IMessagingRepository
import com.dudegenuine.repository.contract.INotificationRepository
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory
import com.dudegenuine.repository.contract.dependency.local.IReceiverFactory
import com.dudegenuine.repository.contract.dependency.local.IWorkerManager
import com.dudegenuine.repository.contract.dependency.remote.IFirebaseManager
import com.dudegenuine.usecase.messaging.*
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IMessageUseCaseModule

/**
 * Mon, 14 Feb 2022
 * WhoKnows by utifmd
 **/
class MessageUseCaseModule(
    repository: IMessagingRepository,
    reposNotifier: INotificationRepository,
    override val preference: IPrefsFactory = repository.preference,
    override val receiver: IReceiverFactory = repository.receiver,
    override val firebase: IFirebaseManager = repository.firebase,
    override val workerManager: IWorkerManager = repository.workerManager): IMessageUseCaseModule {

    override val pushMessaging = PushMessaging(repository)
    override val getMessaging = GetMessaging(repository)
    override val createMessaging = CreateMessaging(repository)
    override val addMessaging = AddMessaging(repository)
    override val removeMessaging = RemoveMessaging(repository)
    override val retrieveMessaging = RetrieveMessaging(reposNotifier)
}