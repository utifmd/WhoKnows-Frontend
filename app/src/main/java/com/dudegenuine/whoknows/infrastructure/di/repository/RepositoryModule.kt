package com.dudegenuine.whoknows.infrastructure.di.repository

import com.dudegenuine.local.manager.IWhoKnowsDatabase
import com.dudegenuine.local.service.IUsersDao
import com.dudegenuine.remote.mapper.contract.*
import com.dudegenuine.remote.service.contract.*
import com.dudegenuine.repository.*
import com.dudegenuine.repository.contract.*
import com.dudegenuine.repository.contract.dependency.local.*
import com.dudegenuine.repository.contract.dependency.remote.IFirebaseManager
import com.dudegenuine.whoknows.infrastructure.di.repository.contract.IRepositoryModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule: IRepositoryModule {

    @Provides
    @Singleton
    override fun provideUserRepository(
        service: IUserService,
        local: IUsersDao,
        mapper: IUserDataMapper,
        prefsFactory: IPrefsFactory,
        receiver: IReceiverFactory
    ): IUserRepository {
        return UserRepository(service, local, mapper, receiver, prefsFactory)
    }

    @Provides
    @Singleton
    override fun provideRoomRepository(
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
        share: IShareLauncher): IRoomRepository {

        return RoomRepository(service, mapper, local, workManager, workRequest, alarmManager, receiver, iPrefsFactory, clip, timer, share)
    }

    @Provides
    @Singleton
    override fun provideQuizRepository(
        service: IQuizService,
        mapper: IQuizDataMapper,
        pref: IPreferenceManager
    ): IQuizRepository {
        return QuizRepository(service, mapper)
    }

    @Provides
    @Singleton
    override fun provideParticipantRepository(
        service: IParticipantService,
        mapperUser: IUserDataMapper,
        mapperPpn: IParticipantDataMapper,
        pref: IPrefsFactory
    ): IParticipantRepository {
        return ParticipantRepository(service, mapperUser, mapperPpn, pref)
    }

    @Provides
    @Singleton
    override fun provideResultRepository(
        service: IResultService,
        mapper: IResultDataMapper,
        pref: IPreferenceManager
    ): IResultRepository {
        return ResultRepository(service, mapper)
    }

    @Provides
    @Singleton
    override fun provideFileRepository(
        service: IFileService,
        mapper: IFileDataMapper,
        pref: IPreferenceManager
    ): IFileRepository {
        return FileRepository(service, mapper)
    }

    @Provides
    @Singleton
    override fun provideMessagingRepository(
        service: IMessagingService,
        mapper: IMessagingDataMapper,
        receiver: IReceiverFactory,
        preference: IPrefsFactory,
        firebase: IFirebaseManager,
        workManager: IWorkerManager): IMessagingRepository {

        return MessagingRepository(service, mapper, receiver, preference, firebase, workManager)
    }

    @Provides
    @Singleton
    override fun provideNotificationRepository(
        service: INotificationService,
        mapper: INotificationDataMapper,
        pref: IPreferenceManager
    ): INotificationRepository {

        return NotificationRepository(service, mapper, pref)
    }
}