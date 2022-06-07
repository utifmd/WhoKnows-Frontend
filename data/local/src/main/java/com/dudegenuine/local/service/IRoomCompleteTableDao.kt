package com.dudegenuine.local.service

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.dudegenuine.local.entity.RoomCompleteTable

/**
 * Tue, 31 May 2022
 * WhoKnows by utifmd
 **/
@Dao
interface IRoomCompleteTableDao/*: ITableDao<RoomCompleteTable> */{
    @Insert(onConflict = REPLACE)
    /*override */suspend fun create(item: RoomCompleteTable)

    @Query("SELECT * FROM rooms_complete WHERE roomId = :param")
    /*override */suspend fun read(param: String): RoomCompleteTable?

    @Update(onConflict = REPLACE, entity = RoomCompleteTable::class)
    /*override */suspend fun update(item: RoomCompleteTable)

    @Delete
    /*override */suspend fun delete(model: RoomCompleteTable)

    @Query("SELECT * FROM rooms_complete WHERE userId = :param LIMIT :batchSize")
    /*override */suspend fun list(param: String, batchSize: Int): List<RoomCompleteTable>

    @Query("DELETE FROM rooms_complete where roomId NOT IN (SELECT roomId from rooms_complete ORDER BY roomId DESC LIMIT :batchSize)")
    /*override */suspend fun trim(batchSize: Int)

    @Query("DELETE FROM rooms_complete")
    /*override */suspend fun clear()
}