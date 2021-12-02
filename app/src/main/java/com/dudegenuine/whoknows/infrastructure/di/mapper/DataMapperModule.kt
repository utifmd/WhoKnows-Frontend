package com.dudegenuine.whoknows.infrastructure.di.mapper

import com.dudegenuine.remote.mapper.UserDataMapper
import com.dudegenuine.remote.mapper.contract.IUserDataMapper
import com.dudegenuine.whoknows.infrastructure.di.mapper.contract.IDataMapperModule
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
object DataMapperModule: IDataMapperModule {

    @Provides
    @Singleton
    override fun provideUserDataMapper(): IUserDataMapper {
        return UserDataMapper()
    }
}