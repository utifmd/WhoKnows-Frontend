package com.dudegenuine.whoknows.infrastructure.di.service

import com.dudegenuine.remote.service.UserService
import com.dudegenuine.remote.service.contract.IUserService
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
}