package com.dudegenuine.whoknows.infrastructure.di.mapper.contract

import android.content.Context
import com.dudegenuine.remote.mapper.contract.*
import com.google.gson.Gson

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
interface IDataMapperModule {
    fun provideUserDataMapper(): IUserDataMapper
    fun provideRoomDataMapper(mapperQuiz: IQuizDataMapper, mapperParticipant: IParticipantDataMapper): IRoomDataMapper
    fun provideQuizDataMapper(gson: Gson): IQuizDataMapper
    fun provideParticipantDataMapper(): IParticipantDataMapper
    fun provideResultDataMapper(): IResultDataMapper
    fun provideFileDataMapper(context: Context): IFileDataMapper
}