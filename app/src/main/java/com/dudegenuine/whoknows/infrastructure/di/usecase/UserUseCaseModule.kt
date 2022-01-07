package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.UserRepository
import com.dudegenuine.usecase.user.*
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
object UserUseCaseModule{ //: IUserUseCaseModule {

    @Provides
    @ViewModelScoped
    fun providePostUserModule(userRepository: UserRepository): PostUser =
        PostUser(userRepository)

    @Provides
    @ViewModelScoped
    fun provideGetUserModule(userRepository: UserRepository): GetUser =
        GetUser(userRepository)

    @Provides
    @ViewModelScoped
    fun providePatchUserModule(userRepository: UserRepository): PatchUser =
        PatchUser(userRepository)

    @Provides
    @ViewModelScoped
    fun provideDeleteUserModule(userRepository: UserRepository): DeleteUser =
        DeleteUser(userRepository)

    @Provides
    @ViewModelScoped
    fun provideGetUsersModule(userRepository: UserRepository): GetUsers =
        GetUsers(userRepository)

    @Provides
    @ViewModelScoped
    fun provideSignInUsersModule(userRepository: UserRepository): SignInUser =
        SignInUser(userRepository)
}