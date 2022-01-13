package com.dudegenuine.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dudegenuine.local.entity.CurrentUser
import com.dudegenuine.local.service.contract.ICurrentUserDao

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/

@Database(entities = [CurrentUser::class], version = 1)
abstract class WhoKnowsDatabase: RoomDatabase() { //, IWhoKnowsDatabase {
    abstract fun currentUserDao(): ICurrentUserDao
}