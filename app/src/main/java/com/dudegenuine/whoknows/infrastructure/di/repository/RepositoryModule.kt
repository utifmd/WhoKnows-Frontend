package com.dudegenuine.whoknows.infrastructure.di.repository

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
        mapper: IUserDataMapper): IUserRepository {
        return UserRepository(service, mapper)
    }

    @Provides
    @Singleton
    override fun provideRoomRepository(
        service: IRoomService,
        mapper: IRoomDataMapper): IRoomRepository {
        return RoomRepository(service, mapper)
    }

    @Provides
    @Singleton
    override fun provideQuizRepository(
        service: IQuizService,
        mapper: IQuizDataMapper): IQuizRepository {
        return QuizRepository(service, mapper)
    }

    @Provides
    @Singleton
    override fun provideParticipantRepository(
        service: IParticipantService,
        mapper: IParticipantDataMapper): IParticipantRepository {
        return ParticipantRepository(service, mapper)
    }

    @Provides
    @Singleton
    override fun provideResultRepository(
        service: IResultService,
        mapper: IResultDataMapper): IResultRepository {
        return ResultRepository(service, mapper)
    }
}