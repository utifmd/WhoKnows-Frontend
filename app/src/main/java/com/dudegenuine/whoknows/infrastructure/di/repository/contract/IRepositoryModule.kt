package com.dudegenuine.whoknows.infrastructure.di.repository.contract

import com.dudegenuine.remote.mapper.contract.*
import com.dudegenuine.remote.service.contract.*
import com.dudegenuine.repository.contract.*

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
interface IRepositoryModule {
    fun provideUserRepository(service: IUserService, mapper: IUserDataMapper): IUserRepository
    fun provideRoomRepository(service: IRoomService, mapper: IRoomDataMapper): IRoomRepository
    fun provideQuizRepository(service: IQuizService, mapper: IQuizDataMapper): IQuizRepository
    fun provideParticipantRepository(service: IParticipantService, mapper: IParticipantDataMapper): IParticipantRepository
    fun provideResultRepository(service: IResultService, mapper: IResultDataMapper): IResultRepository
    fun provideFileRepository(service: IFileService, mapper: IFileDataMapper): IFileRepository
}