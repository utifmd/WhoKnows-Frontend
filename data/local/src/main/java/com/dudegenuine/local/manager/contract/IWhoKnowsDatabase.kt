package com.dudegenuine.local.manager.contract

import com.dudegenuine.local.service.contract.ICurrentUserDao

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
interface IWhoKnowsDatabase {
    fun currentUserDao(): ICurrentUserDao

    companion object {
        const val DATABASE_NAME = "WhoKnowsDatabase"
    }
}