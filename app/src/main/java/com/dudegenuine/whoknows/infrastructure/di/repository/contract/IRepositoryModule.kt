package com.dudegenuine.whoknows.infrastructure.di.repository.contract

import com.dudegenuine.local.api.IClipboardManager
import com.dudegenuine.local.api.IPreferenceManager
import com.dudegenuine.local.api.IReceiverFactory
import com.dudegenuine.local.service.contract.ICurrentBoardingDao
import com.dudegenuine.local.service.contract.ICurrentUserDao
import com.dudegenuine.remote.mapper.contract.*
import com.dudegenuine.remote.service.contract.*
import com.dudegenuine.repository.contract.*

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
interface IRepositoryModule {
    fun provideUserRepository(
        service: IUserService,
        local: ICurrentUserDao,
        mapper: IUserDataMapper,
        pref: IPreferenceManager,
        receiver: IReceiverFactory): IUserRepository

    fun provideRoomRepository(
        service: IRoomService,
        receiver: IReceiverFactory,
        local: ICurrentBoardingDao,
        mapper: IRoomDataMapper,
        pref: IPreferenceManager,
        clip: IClipboardManager): IRoomRepository

    fun provideQuizRepository(
        service: IQuizService,
        mapper: IQuizDataMapper,
        pref: IPreferenceManager
    ): IQuizRepository

    fun provideParticipantRepository(
        service: IParticipantService,
        mapper: IParticipantDataMapper,
        pref: IPreferenceManager
    ): IParticipantRepository

    fun provideResultRepository(
        service: IResultService,
        mapper: IResultDataMapper,
        pref: IPreferenceManager
    ): IResultRepository

    fun provideFileRepository(
        service: IFileService,
        mapper: IFileDataMapper,
        pref: IPreferenceManager
    ): IFileRepository

    fun provideMessagingRepository(
        service: IMessagingService,
        mapper: IMessagingDataMapper,
        pref: IPreferenceManager,
        receiver: IReceiverFactory
    ): IMessagingRepository

    fun provideNotificationRepository(
        service: INotificationService,
        mapper: INotificationDataMapper,
        pref: IPreferenceManager
    ): INotificationRepository
}