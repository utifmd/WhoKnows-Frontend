package com.dudegenuine.whoknows.infrastructure.di.mapper.contract

import android.content.Context
import com.dudegenuine.remote.mapper.contract.*
import com.google.gson.Gson

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
interface IDataMapperModule {
    fun provideUserDataMapper(gson: Gson): IUserDataMapper
    fun provideRoomDataMapper(gson: Gson, mapperQuiz: IQuizDataMapper, mapperParticipant: IParticipantDataMapper): IRoomDataMapper
    fun provideQuizDataMapper(gson: Gson): IQuizDataMapper
    fun provideParticipantDataMapper(gson: Gson): IParticipantDataMapper
    fun provideResultDataMapper(gson: Gson): IResultDataMapper
    fun provideFileDataMapper(context: Context): IFileDataMapper
}