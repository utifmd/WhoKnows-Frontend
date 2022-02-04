package com.dudegenuine.whoknows.infrastructure.di.repository

import com.dudegenuine.local.manager.contract.IClipboardManager
import com.dudegenuine.local.manager.contract.IPreferenceManager
import com.dudegenuine.local.service.contract.ICurrentUserDao
import com.dudegenuine.remote.mapper.contract.*
import com.dudegenuine.remote.service.contract.*
import com.dudegenuine.repository.*
import com.dudegenuine.repository.contract.*
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
        dao: ICurrentUserDao,
        pref: IPreferenceManager,
        mapper: IUserDataMapper
    ): IUserRepository {
        return UserRepository(service, dao, pref, mapper)
    }

    @Provides
    @Singleton
    override fun provideRoomRepository(
        service: IRoomService,
        mapper: IRoomDataMapper,
        pref: IPreferenceManager,
        clip: IClipboardManager): IRoomRepository {

        return RoomRepository(service, mapper, pref, clip)
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
        mapper: IParticipantDataMapper,
        pref: IPreferenceManager
    ): IParticipantRepository {
        return ParticipantRepository(service, mapper)
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
}