package com.dudegenuine.whoknows.infrastructure.di.mapper.contract

import com.dudegenuine.remote.mapper.contract.*

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
interface IDataMapperModule {
    fun provideUserDataMapper(): IUserDataMapper
    fun provideRoomDataMapper(): IRoomDataMapper
//    fun provideQuizDataMapper(): IQuizDataMapper
//    fun provideParticipantDataMapper(): IParticipantDataMapper
//    fun provideResultDataMapper(): IResultDataMapper
}