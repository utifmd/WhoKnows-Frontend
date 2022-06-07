package com.dudegenuine.whoknows.infrastructure.di.repository.contract

import com.dudegenuine.local.manager.IWhoKnowsDatabase
import com.dudegenuine.local.service.IUsersDao
import com.dudegenuine.remote.mapper.contract.*
import com.dudegenuine.remote.service.contract.*
import com.dudegenuine.repository.contract.*
import com.dudegenuine.repository.contract.dependency.local.*
import com.dudegenuine.repository.contract.dependency.remote.IFirebaseManager

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
interface IRepositoryModule {
    fun provideUserRepository(
        service: IUserService,
        local: IUsersDao,
        mapper: IUserDataMapper,
        prefsFactory: IPrefsFactory,
        receiver: IReceiverFactory
    ): IUserRepository

    fun provideRoomRepository(
        service: IRoomService,
        receiver: IReceiverFactory,
        local: IWhoKnowsDatabase,
        mapper: IRoomDataMapper,
        workManager: IWorkerManager,
        alarmManager: IAlarmManager,
        workRequest: ITokenWorkManager,
        iPrefsFactory: IPrefsFactory,
        clip: IClipboardManager,
        timer: ITimerLauncher,
        share: IShareLauncher
    ): IRoomRepository

    fun provideQuizRepository(
        service: IQuizService,
        mapper: IQuizDataMapper,
        pref: IPreferenceManager
    ): IQuizRepository

    fun provideParticipantRepository(
        service: IParticipantService,
        mapperUser: IUserDataMapper,
        mapperPpn: IParticipantDataMapper,
        pref: IPrefsFactory
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
        receiver: IReceiverFactory,
        preference: IPrefsFactory,
        firebase: IFirebaseManager,
        workManager: IWorkerManager
    ): IMessagingRepository

    fun provideNotificationRepository(
        service: INotificationService,
        mapper: INotificationDataMapper,
        pref: IPreferenceManager
    ): INotificationRepository
}