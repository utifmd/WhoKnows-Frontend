package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import android.content.Context
import com.dudegenuine.repository.contract.*

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
    fun provideUserUseCaseModule(repository: IUserRepository): IUserUseCaseModule
    fun provideNotificationUseCaseModule(repository: INotificationRepository): INotificationUseCaseModule
}