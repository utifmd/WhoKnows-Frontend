package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.repository.contract.IUserRepository

/**
 * Fri, 07 Jan 2022
 * WhoKnows by utifmd
 **/
interface IUseCaseModule {
    fun provideUserUseCaseModule(repository: IUserRepository): IUserUseCaseModule
}