package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.UserRepository
import com.dudegenuine.usecase.user.*
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
    override fun providePostUserModule(userRepository: UserRepository): PostUser =
        PostUser(userRepository)

    @Provides
    @Singleton
    override fun provideGetUserModule(userRepository: UserRepository): GetUser =
        GetUser(userRepository)

    @Provides
    @Singleton
    override fun providePatchUserModule(userRepository: UserRepository): PatchUser =
        PatchUser(userRepository)

    @Provides
    @Singleton
    override fun provideDeleteUserModule(userRepository: UserRepository): DeleteUser =
        DeleteUser(userRepository)

    @Provides
    @Singleton
    override fun provideGetUsersModule(userRepository: UserRepository): GetUsers =
        GetUsers(userRepository)

    @Provides
    @Singleton
    override fun provideSignInUsersModule(userRepository: UserRepository): SignInUser =
        SignInUser(userRepository)
}