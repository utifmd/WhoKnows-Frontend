package com.dudegenuine.whoknows.infrastructure.di.service

import com.dudegenuine.remote.service.*
import com.dudegenuine.remote.service.contract.*
import com.dudegenuine.whoknows.infrastructure.di.service.contract.IServiceModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
@Module
@InstallIn(SingletonComponent::class)
object ServiceModule: IServiceModule {

    @Provides
    @Singleton
    override fun provideUserService(network: Retrofit.Builder): IUserService {
        return network.build().create(UserService::class.java)
    }

    @Provides
    @Singleton
    override fun provideRoomService(network: Retrofit.Builder): IRoomService {
        return network.build().create(RoomService::class.java)
    }

    @Provides
    @Singleton
    override fun provideQuizService(network: Retrofit.Builder): IQuizService {
        return network.build().create(QuizService::class.java)
    }

    @Provides
    @Singleton
    override fun provideParticipantService(network: Retrofit.Builder): IParticipantService {
        return network.build().create(ParticipantService::class.java)
    }

    @Provides
    @Singleton
    override fun provideResultService(network: Retrofit.Builder): IResultService {
        return network.build().create(ResultService::class.java)
    }

    @Provides
    @Singleton
    override fun provideFileService(network: Retrofit.Builder): IFileService {
        return network.build().create(FileService::class.java)
    }
}