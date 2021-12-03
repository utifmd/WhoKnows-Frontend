package com.dudegenuine.whoknows.infrastructure.di.repository.contract

import com.dudegenuine.remote.mapper.contract.IUserDataMapper
import com.dudegenuine.remote.service.contract.IUserService
import com.dudegenuine.repository.contract.IUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
interface IRepositoryModule {
    fun provideUserRepository(service: IUserService, mapper: IUserDataMapper): IUserRepository
}