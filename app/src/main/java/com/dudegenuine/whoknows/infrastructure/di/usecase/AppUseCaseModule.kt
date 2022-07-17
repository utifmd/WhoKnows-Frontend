package com.dudegenuine.whoknows.infrastructure.di.usecase

import android.content.Context
import com.dudegenuine.repository.contract.*
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Sat, 08 Jan 2022
 * WhoKnows by utifmd
 **/
@Module
@InstallIn(ViewModelComponent::class)
object AppUseCaseModule: IAppUseCaseModule {
    @Provides
    @ViewModelScoped
    override fun provideFileUseCaseModule(
        @ApplicationContext context: Context,
        repository: IFileRepository): IFileUseCaseModule =
        FileUseCaseModule(context, repository)

    @Provides
    @ViewModelScoped
    override fun provideMessagingUseCaseModule(
        repository: IMessagingRepository,
        reposNotifier: INotificationRepository): IMessageUseCaseModule {
        return MessageUseCaseModule(repository, reposNotifier)
    }

    @Provides
    @ViewModelScoped
    override fun provideParticipantUseCaseModule(
        reposParticipant: IParticipantRepository,
        reposRoom: IRoomRepository,
        reposResult: IResultRepository,
        reposNotification: INotificationRepository,
        reposMessaging: IMessagingRepository): IParticipantUseCaseModule =

        ParticipantUseCaseModule(reposParticipant, reposRoom, reposResult, reposNotification, reposMessaging)


    @Provides
    @ViewModelScoped
    override fun provideQuizUseCaseModule(
        repository: IQuizRepository): IQuizUseCaseModule =
        QuizUseCaseModule(repository)

    @Provides
    @ViewModelScoped
    override fun provideResultUseCaseModule(
        repository: IResultRepository): IResultUseCaseModule =
        ResultUseCaseModule(repository)

    @Provides
    @ViewModelScoped
    override fun provideImpressionUseCaseModule(
        repoImpression: IImpressionRepository,
        reposNotify: INotificationRepository,
        repoMessaging: IMessagingRepository
    ): IImpressionUseCaseModule {
        return ImpressionUseCaseModule(repoImpression, reposNotify, repoMessaging)
    }

    @Provides
    @ViewModelScoped
    override fun provideRoomUseCaseModule(
        repository: IRoomRepository,
        reposUser: IUserRepository,
        reposResult: IResultRepository,
        reposNotifier: INotificationRepository,
        reposFile: IFileRepository,
        reposMessaging: IMessagingRepository
    ): IRoomUseCaseModule =
        RoomUseCaseModule(repository, reposUser, reposResult, reposNotifier, reposFile, reposMessaging)

    @Provides
    @ViewModelScoped
    override fun provideUserUseCaseModule(
        userRepository: IUserRepository,
        roomRepository: IRoomRepository,
        messagingRepository: IMessagingRepository,
        preferences: IPrefsFactory): IUserUseCaseModule = UserUseCaseModule(
            userRepository, roomRepository, messagingRepository
    )

    @Provides
    @ViewModelScoped
    override fun provideNotificationUseCaseModule(
        repository: INotificationRepository, reposMessaging: IMessagingRepository): INotificationUseCaseModule {
        return NotificationUseCaseModule(repository, reposMessaging)
    }
}