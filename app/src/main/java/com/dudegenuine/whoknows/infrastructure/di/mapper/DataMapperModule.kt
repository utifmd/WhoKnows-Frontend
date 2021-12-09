package com.dudegenuine.whoknows.infrastructure.di.mapper

import com.dudegenuine.remote.mapper.*
import com.dudegenuine.remote.mapper.contract.*
import com.dudegenuine.whoknows.infrastructure.di.mapper.contract.IDataMapperModule
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
object DataMapperModule: IDataMapperModule {

    @Provides
    @Singleton
    override fun provideUserDataMapper(): IUserDataMapper {
        return UserDataMapper()
    }

    @Provides
    @Singleton
    override fun provideRoomDataMapper(): IRoomDataMapper {
        return RoomDataMapper()
    }

    @Provides
    @Singleton
    override fun provideQuizDataMapper(): IQuizDataMapper {
        return QuizDataMapper()
    }

    @Provides
    @Singleton
    override fun provideParticipantDataMapper(): IParticipantDataMapper {
        return ParticipantDataMapper()
    }

    @Provides
    @Singleton
    override fun provideResultDataMapper(): IResultDataMapper {
        return ResultDataMapper()
    }
}