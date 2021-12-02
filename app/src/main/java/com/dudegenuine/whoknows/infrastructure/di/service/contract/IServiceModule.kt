package com.dudegenuine.whoknows.infrastructure.di.service.contract

import com.dudegenuine.remote.service.contract.IUserService
import retrofit2.Retrofit

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
interface IServiceModule {
    fun provideUserService(network: Retrofit.Builder): IUserService
}