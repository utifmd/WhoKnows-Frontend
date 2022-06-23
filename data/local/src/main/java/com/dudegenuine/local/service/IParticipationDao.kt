package com.dudegenuine.local.service

import androidx.room.*
import com.dudegenuine.local.entity.ParticipationTable

/**
 * Fri, 18 Feb 2022
 * WhoKnows by utifmd
 **/
@Dao
interface IParticipationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(participationTable: ParticipationTable)

    @Update(entity = ParticipationTable::class)
    suspend fun update(participationTable: ParticipationTable)

    @Query("SELECT * FROM participation WHERE participantId = :participantId")
    suspend fun read(participantId: String): ParticipationTable?

    @Query("SELECT * FROM participation LIMIT 1")
    suspend fun read(): ParticipationTable?

    @Delete
    suspend fun delete(participation: ParticipationTable)

    @Query("DELETE FROM participation")
    suspend fun delete()
}