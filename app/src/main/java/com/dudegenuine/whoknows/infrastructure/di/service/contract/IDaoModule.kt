package com.dudegenuine.whoknows.infrastructure.di.service.contract

import com.dudegenuine.local.database.WhoKnowsDatabase
import com.dudegenuine.local.service.contract.ICurrentUserDao

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
interface IDaoModule {
    fun provideCurrentUserDaoModule(localDatabase: WhoKnowsDatabase): ICurrentUserDao
}