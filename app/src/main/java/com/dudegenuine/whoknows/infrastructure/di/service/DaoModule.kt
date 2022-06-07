package com.dudegenuine.whoknows.infrastructure.di.service

import com.dudegenuine.local.manager.IWhoKnowsDatabase
import com.dudegenuine.local.service.IParticipationDao
import com.dudegenuine.local.service.IRoomCensoredTableDao
import com.dudegenuine.local.service.IRoomCompleteTableDao
import com.dudegenuine.local.service.IUsersDao
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
        localDatabaseI: IWhoKnowsDatabase): IUsersDao {

        return localDatabaseI.daoUsers()
    }

    @Provides
    @Singleton
    override fun provideCurrentBoardingDaoModule(
        localDatabaseI: IWhoKnowsDatabase): IParticipationDao {

        return localDatabaseI.daoBoarding()
    }

    @Provides
    @Singleton
    override fun provideRoomCensoredDaoModule(
        localDatabase: IWhoKnowsDatabase): IRoomCensoredTableDao {
        return localDatabase.daoRoomCensored()
    }

    @Provides
    @Singleton
    override fun provideRoomCompleteDaoModule(
        localDatabase: IWhoKnowsDatabase): IRoomCompleteTableDao {
        return localDatabase.daoRoomComplete()
    }
}