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
        repository: IMessagingRepository): IMessageUseCaseModule {
        return MessageUseCaseModule(repository)
    }

    @Provides
    @ViewModelScoped
    override fun provideParticipantUseCaseModule(
        repository: IParticipantRepository): IParticipantUseCaseModule =
        ParticipantUseCaseModule(repository)


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
    override fun provideRoomUseCaseModule(
        repository: IRoomRepository): IRoomUseCaseModule =
        RoomUseCaseModule(repository)

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
        repository: INotificationRepository): INotificationUseCaseModule {
        return NotificationUseCaseModule(repository)
    }
}