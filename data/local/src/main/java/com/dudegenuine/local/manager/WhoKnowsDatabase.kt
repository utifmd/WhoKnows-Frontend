package com.dudegenuine.local.manager

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dudegenuine.local.entity.BoardingQuizTable
import com.dudegenuine.local.entity.UserTable
import com.dudegenuine.local.mapper.*
import com.dudegenuine.local.service.contract.ICurrentBoardingDao
import com.dudegenuine.local.service.contract.ICurrentUserDao

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/

@Database(
    entities = [
        UserTable::class,
        BoardingQuizTable::class
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(
    DateConverter::class,
    ListStringConverter::class,
    BoardingConverter::class,
    ParticipantConverter::class,
    QuizConverter::class,
    UserCensoredConverter::class,
    RoomCensoredConverter::class,
    AnswerConverter::class,
    PossibleAnswerConverter::class
)

abstract class WhoKnowsDatabase: RoomDatabase() { //, IWhoKnowsDatabase {
    abstract fun currentUserDao(): ICurrentUserDao
    abstract fun currentBoardingDao(): ICurrentBoardingDao
}