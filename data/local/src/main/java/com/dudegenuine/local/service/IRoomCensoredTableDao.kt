package com.dudegenuine.local.service

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.dudegenuine.local.entity.RoomCensoredTable

/**
 * Tue, 31 May 2022
 * WhoKnows by utifmd
 **/
@Dao
interface IRoomCensoredTableDao/*: ITableDao<RoomCensoredTable> */{
    @Insert(onConflict = REPLACE)
    /*override */suspend fun createAll(list: List<RoomCensoredTable>)

    @Query("SELECT * FROM rooms_censored WHERE roomId = :param")
    /*override */suspend fun read(param: String): RoomCensoredTable?
    @Update(onConflict = REPLACE, entity = RoomCensoredTable::class)
    /*override */suspend fun update(item: RoomCensoredTable)

    @Delete
    /*override */suspend fun delete(model: RoomCensoredTable)

    @Query("SELECT * FROM rooms_censored WHERE userId = :param LIMIT :batchSize")
    /*override */suspend fun list(param: String, batchSize: Int): List<RoomCensoredTable>

    @Query("SELECT * FROM rooms_censored")
    /*override */suspend fun list(): List<RoomCensoredTable>

    @Query("DELETE FROM rooms_censored where roomId NOT IN (SELECT roomId from rooms_censored ORDER BY roomId DESC LIMIT :batchSize)")
    /*override */suspend fun trim(batchSize: Int)

    @Query("DELETE FROM rooms_censored")
    /*override */suspend fun clear()
}