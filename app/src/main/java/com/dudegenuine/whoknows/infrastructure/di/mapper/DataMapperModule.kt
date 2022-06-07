package com.dudegenuine.whoknows.infrastructure.di.mapper

import android.content.Context
import android.util.Log
import com.dudegenuine.remote.mapper.*
import com.dudegenuine.remote.mapper.contract.*
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory
import com.dudegenuine.whoknows.infrastructure.di.mapper.contract.IDataMapperModule
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    override fun provideUserDataMapper(
        gson: Gson, preference: IPrefsFactory): IUserDataMapper {
        Log.d("provideUserDataMapper", " ${preference.userId}")
        return UserDataMapper(gson, preference.userId)
    }

    @Provides
    @Singleton
    override fun provideRoomDataMapper(
        gson: Gson,
        preference: IPrefsFactory,
        mapperUser: IUserDataMapper,
        mapperQuiz: IQuizDataMapper,
        mapperImpression: IImpressionDataMapper,
        mapperParticipant: IParticipantDataMapper): IRoomDataMapper {

        return RoomDataMapper(preference.userId, gson, mapperUser, mapperQuiz, mapperParticipant)
    }

    @Provides
    @Singleton
    override fun provideQuizDataMapper(gson: Gson, mapper: IUserDataMapper): IQuizDataMapper {

        return QuizDataMapper(gson, mapper)
    }

    @Provides
    @Singleton
    override fun provideParticipantDataMapper(
        preference: IPrefsFactory,
        gson: Gson,
        mapper: IUserDataMapper
    ): IParticipantDataMapper {

        return ParticipantDataMapper(preference.userId, mapper)
    }

    @Provides
    @Singleton
    override fun provideResultDataMapper(gson: Gson, mapper: IUserDataMapper): IResultDataMapper {

        return ResultDataMapper(mapper, gson)
    }

    @Provides
    @Singleton
    override fun provideFileDataMapper(
        @ApplicationContext context: Context): IFileDataMapper {

        return FileDataMapper(context)
    }

    @Provides
    @Singleton
    override fun provideMessagingDataMapper(
        @ApplicationContext context: Context,
        gson: Gson): IMessagingDataMapper {

        return MessagingDataMapper(gson)
    }

    @Provides
    @Singleton
    override fun provideNotificationDataMapper(
        @ApplicationContext context: Context,
        mapper: IUserDataMapper): INotificationDataMapper {

        return NotificationDataMapper(mapper)
    }

    @Provides
    @Singleton
    override fun provideImpressionDataMapper(
        @ApplicationContext context: Context,
        gson: Gson): IImpressionDataMapper {
        return ImpressionDataMapper()
    }
}