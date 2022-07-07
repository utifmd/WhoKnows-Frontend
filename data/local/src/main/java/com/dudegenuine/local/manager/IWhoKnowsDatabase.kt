package com.dudegenuine.local.manager

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dudegenuine.local.entity.ParticipationTable
import com.dudegenuine.local.entity.RoomCensoredTable
import com.dudegenuine.local.entity.RoomCompleteTable
import com.dudegenuine.local.entity.UserTable
import com.dudegenuine.local.mapper.*
import com.dudegenuine.local.service.IParticipationDao
import com.dudegenuine.local.service.IRoomCensoredTableDao
import com.dudegenuine.local.service.IRoomCompleteTableDao
import com.dudegenuine.local.service.IUsersDao

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/

@Database(
    entities = [
        UserTable::class,
        RoomCensoredTable::class,
        RoomCompleteTable::class,
        ParticipationTable::class
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(
    DateConverter::class,
    ListStringConverter::class,
    ParticipationPageConverter::class,
    QuizzesConverter::class,
    ParticipantConverter::class,
    NotificationConverter::class,
    QuizConverter::class,
    UserCensoredConverter::class,
    RoomCensoredConverter::class,
    ImpressionsConverter::class,
    AnswerConverter::class,
    PossibleAnswerConverter::class
)

abstract class IWhoKnowsDatabase: RoomDatabase() {
    abstract fun daoUsers(): IUsersDao
    abstract fun daoBoarding(): IParticipationDao
    abstract fun daoRoomComplete(): IRoomCompleteTableDao
    abstract fun daoRoomCensored(): IRoomCensoredTableDao
}