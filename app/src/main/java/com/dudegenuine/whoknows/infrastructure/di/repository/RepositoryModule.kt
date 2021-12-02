package com.dudegenuine.whoknows.infrastructure.di.repository

import com.dudegenuine.remote.mapper.contract.IUserDataMapper
import com.dudegenuine.remote.service.contract.IUserService
import com.dudegenuine.repository.UserRepository
import com.dudegenuine.repository.contract.IUserRepository
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
}