package com.dudegenuine.whoknows.infrastructure.di.mapper.contract

import android.content.Context
import com.dudegenuine.remote.mapper.contract.*
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory
import com.google.gson.Gson

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
interface IDataMapperModule {

    fun provideUserDataMapper(
        gson: Gson, preference: IPrefsFactory): IUserDataMapper

    fun provideRoomDataMapper(
        gson: Gson,
        preference: IPrefsFactory,
        mapperUser: IUserDataMapper,
        mapperQuiz: IQuizDataMapper,
        mapperImpression: IImpressionDataMapper,
        mapperParticipant: IParticipantDataMapper): IRoomDataMapper

    fun provideQuizDataMapper(
        gson: Gson,
        mapper: IUserDataMapper): IQuizDataMapper

    fun provideParticipantDataMapper(
        preference: IPrefsFactory,
        gson: Gson, mapper: IUserDataMapper): IParticipantDataMapper

    fun provideResultDataMapper(
        gson: Gson, mapper: IUserDataMapper): IResultDataMapper

    fun provideFileDataMapper(
        context: Context): IFileDataMapper

    fun provideMessagingDataMapper(
        context: Context, gson: Gson): IMessagingDataMapper

    fun provideNotificationDataMapper(
        context: Context, mapper: IUserDataMapper): INotificationDataMapper

    fun provideImpressionDataMapper(context: Context, gson: Gson): IImpressionDataMapper
}