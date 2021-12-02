package com.dudegenuine.whoknows.infrastructure.di.mapper.contract

import com.dudegenuine.remote.mapper.contract.IUserDataMapper

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
interface IDataMapperModule {
    fun provideUserDataMapper(): IUserDataMapper
}