package com.dudegenuine.whoknows.infrastructure.di.usecase

/**
 * Sat, 16 Jul 2022
 * WhoKnows by utifmd
class MessagingSingletonUseCaseModule(
    reposMessaging: IMessagingRepository, reposRoom: IRoomRepository,
    reposNotifier: INotificationRepository): IMessagingSingletonUseCaseModule {

    override val retrieveMessaging: RetrieveMessaging =
        RetrieveMessaging(reposRoom, reposMessaging, reposNotifier)
}
 **/
