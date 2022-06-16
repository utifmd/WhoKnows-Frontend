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
    fun provideMessagingUseCaseModule(repository: IMessagingRepository): IMessageUseCaseModule
    fun provideParticipantUseCaseModule(repository: IParticipantRepository): IParticipantUseCaseModule
    fun provideQuizUseCaseModule(repository: IQuizRepository): IQuizUseCaseModule
    fun provideResultUseCaseModule(repository: IResultRepository): IResultUseCaseModule
    fun provideRoomUseCaseModule(repository: IRoomRepository): IRoomUseCaseModule
    fun provideUserUseCaseModule(
        userRepository: IUserRepository,
        roomRepository: IRoomRepository,
        messagingRepository: IMessagingRepository,
        preferences: IPrefsFactory,
    ): IUserUseCaseModule
    fun provideNotificationUseCaseModule(repository: INotificationRepository): INotificationUseCaseModule

    companion object {
        const val EMPTY_STRING = ""
    }
}