package com.dudegenuine.local.manager

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dudegenuine.local.entity.CurrentRoomState
import com.dudegenuine.local.entity.CurrentUser
import com.dudegenuine.local.mapper.BoardingConverter
import com.dudegenuine.local.mapper.DateConverter
import com.dudegenuine.local.mapper.ParticipantConverter
import com.dudegenuine.local.service.contract.ICurrentBoardingDao
import com.dudegenuine.local.service.contract.ICurrentUserDao

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/

@Database(
    entities = [
        CurrentUser::class,
        CurrentRoomState::class],
    version = 1,
    exportSchema = false)

@TypeConverters(
    DateConverter::class,
    ParticipantConverter::class,
    BoardingConverter::class)

abstract class WhoKnowsDatabase: RoomDatabase() { //, IWhoKnowsDatabase {
    abstract fun currentUserDao(): ICurrentUserDao
    abstract fun currentBoardingDao(): ICurrentBoardingDao
}