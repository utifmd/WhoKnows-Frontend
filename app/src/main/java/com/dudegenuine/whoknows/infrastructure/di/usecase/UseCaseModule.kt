package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.UserRepository
import com.dudegenuine.usecase.user.GetUser
import com.dudegenuine.usecase.user.GetUsers
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUseCaseModule
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
object UseCaseModule: IUseCaseModule {
    @Provides
    @Singleton
    override fun provideReadUserModule(userRepository: UserRepository): GetUser {
        return GetUser(userRepository)
    }

    @Provides
    @Singleton
    override fun provideReadUsersModule(userRepository: UserRepository): GetUsers {
        return GetUsers(userRepository)
    }
}