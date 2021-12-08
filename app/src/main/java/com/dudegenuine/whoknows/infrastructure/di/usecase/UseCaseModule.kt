package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.UserRepository
import com.dudegenuine.usecase.user.*
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUseCaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule: IUseCaseModule {
    @Provides
    @ViewModelScoped
    override fun providePostUserModule(userRepository: UserRepository): PostUser =
        PostUser(userRepository)

    @Provides
    @ViewModelScoped
    override fun provideGetUserModule(userRepository: UserRepository): GetUser =
        GetUser(userRepository)

    @Provides
    @ViewModelScoped
    override fun providePatchUserModule(userRepository: UserRepository): PatchUser =
        PatchUser(userRepository)

    @Provides
    @ViewModelScoped
    override fun provideDeleteUserModule(userRepository: UserRepository): DeleteUser =
        DeleteUser(userRepository)

    @Provides
    @ViewModelScoped
    override fun provideGetUsersModule(userRepository: UserRepository): GetUsers =
        GetUsers(userRepository)

    @Provides
    @ViewModelScoped
    override fun provideSignInUsersModule(userRepository: UserRepository): SignInUser =
        SignInUser(userRepository)
}