package com.dudegenuine.whoknows.infrastructure.di.service

import com.dudegenuine.local.database.WhoKnowsDatabase
import com.dudegenuine.local.service.contract.ICurrentUserDao
import com.dudegenuine.whoknows.infrastructure.di.service.contract.IDaoModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
@Module
@InstallIn(SingletonComponent::class)
object DaoModule: IDaoModule {

    @Provides
    @Singleton
    override fun provideCurrentUserDaoModule(
        localDatabase: WhoKnowsDatabase): ICurrentUserDao {

        return localDatabase.currentUserDao()
    }
}