package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.repository.UserRepository
import com.dudegenuine.usecase.user.GetUser

/**
 * Fri, 03 Dec 2021
 * WhoKnows by utifmd
 **/
interface IUseCaseModule {
    fun provideReadUserModule(userRepository: UserRepository): GetUser
}