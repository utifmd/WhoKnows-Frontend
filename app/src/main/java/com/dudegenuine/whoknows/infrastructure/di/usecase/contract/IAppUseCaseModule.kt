package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import android.content.Context
import com.dudegenuine.repository.contract.*
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory

/**
 * Fri, 07 Jan 2022
 * WhoKnows by utifmd
 **/
interface IAppUseCaseModule {
    fun provideFileUseCaseModule(context: Context, repository: IFileRepository): IFileUseCaseModule
    fun provideMessagingUseCaseModule(
        repository: IMessagingRepository,
        reposNotifier: INotificationRepository): IMessageUseCaseModule
    fun provideParticipantUseCaseModule(
        reposParticipant: IParticipantRepository,
        reposRoom: IRoomRepository,
        reposResult: IResultRepository,
        reposNotification: INotificationRepository,
        reposMessaging: IMessagingRepository,
    ): IParticipantUseCaseModule
    fun provideQuizUseCaseModule(repository: IQuizRepository): IQuizUseCaseModule
    fun provideResultUseCaseModule(repository: IResultRepository): IResultUseCaseModule
    fun provideImpressionUseCaseModule(
        repoImpression: IImpressionRepository,
        reposNotify: INotificationRepository,
        repoMessaging: IMessagingRepository): IImpressionUseCaseModule
    fun provideRoomUseCaseModule(
        repository: IRoomRepository,
        reposUser: IUserRepository,
        reposResult: IResultRepository,
        reposNotifier: INotificationRepository,
        reposFile: IFileRepository,
        reposMessaging: IMessagingRepository): IRoomUseCaseModule
    fun provideUserUseCaseModule(
        userRepository: IUserRepository,
        roomRepository: IRoomRepository,
        messagingRepository: IMessagingRepository,
        preferences: IPrefsFactory,
    ): IUserUseCaseModule
    fun provideNotificationUseCaseModule(repository: INotificationRepository, reposMessaging: IMessagingRepository): INotificationUseCaseModule

    companion object {
        const val EMPTY_STRING = ""
    }
}