package com.dudegenuine.whoknows.infrastructure.di.service.contract

import com.dudegenuine.local.manager.IWhoKnowsDatabase
import com.dudegenuine.local.service.IParticipationDao
import com.dudegenuine.local.service.IUsersDao
import com.dudegenuine.local.service.IRoomCensoredTableDao
import com.dudegenuine.local.service.IRoomCompleteTableDao

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
interface IDaoModule {
    fun provideCurrentUserDaoModule(localDatabaseI: IWhoKnowsDatabase): IUsersDao
    fun provideCurrentBoardingDaoModule(localDatabaseI: IWhoKnowsDatabase): IParticipationDao
    fun provideRoomCensoredDaoModule(localDatabase: IWhoKnowsDatabase): IRoomCensoredTableDao
    fun provideRoomCompleteDaoModule(localDatabase: IWhoKnowsDatabase): IRoomCompleteTableDao
}