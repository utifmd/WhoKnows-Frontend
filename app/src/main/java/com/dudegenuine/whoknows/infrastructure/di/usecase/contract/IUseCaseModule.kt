package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.repository.UserRepository
import com.dudegenuine.usecase.user.*

/**
 * Fri, 03 Dec 2021
 * WhoKnows by utifmd
 **/
interface IUseCaseModule {
    fun providePostUserModule(userRepository: UserRepository): PostUser
    fun provideGetUserModule(userRepository: UserRepository): GetUser
    fun providePatchUserModule(userRepository: UserRepository): PatchUser
    fun provideDeleteUserModule(userRepository: UserRepository): DeleteUser
    fun provideGetUsersModule(userRepository: UserRepository): GetUsers
    fun provideSignInUsersModule(userRepository: UserRepository): SignInUser
}