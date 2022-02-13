package com.dudegenuine.whoknows.infrastructure.di.repository.contract

import com.dudegenuine.local.api.IClipboardManager
import com.dudegenuine.local.api.IPreferenceManager
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
        service: IUserService, dao: ICurrentUserDao, pref: IPreferenceManager, mapper: IUserDataMapper): IUserRepository

    fun provideRoomRepository(
        service: IRoomService, mapper: IRoomDataMapper, pref: IPreferenceManager, clip: IClipboardManager
    ): IRoomRepository

    fun provideQuizRepository(
        service: IQuizService, mapper: IQuizDataMapper, pref: IPreferenceManager
    ): IQuizRepository

    fun provideParticipantRepository(
        service: IParticipantService, mapper: IParticipantDataMapper, pref: IPreferenceManager
    ): IParticipantRepository

    fun provideResultRepository(
        service: IResultService, mapper: IResultDataMapper, pref: IPreferenceManager
    ): IResultRepository

    fun provideFileRepository(
        service: IFileService, mapper: IFileDataMapper, pref: IPreferenceManager
    ): IFileRepository

    fun provideNotificationRepository(
        serviceOnPremise: INotificationService, serviceOnCloud: IPushNotificationService, mapper: INotificationDataMapper, pref: IPreferenceManager
    ): INotificationRepository
}