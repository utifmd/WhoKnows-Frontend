package com.dudegenuine.local.service.contract

import androidx.room.*
import com.dudegenuine.local.entity.CurrentRoomState

/**
 * Fri, 18 Feb 2022
 * WhoKnows by utifmd
 **/
@Dao
interface ICurrentBoardingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(roomStateState: CurrentRoomState)

    @Update(entity = CurrentRoomState::class)
    suspend fun update(roomStateState: CurrentRoomState)

    @Query("SELECT * FROM currentRoomState WHERE participantId = :participantId")
    suspend fun read(participantId: String): CurrentRoomState?

    @Delete
    suspend fun delete(roomStateState: CurrentRoomState)
}