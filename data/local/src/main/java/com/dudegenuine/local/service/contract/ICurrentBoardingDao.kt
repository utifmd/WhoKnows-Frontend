package com.dudegenuine.local.service.contract

import androidx.room.*
import com.dudegenuine.local.entity.BoardingQuizTable

/**
 * Fri, 18 Feb 2022
 * WhoKnows by utifmd
 **/
@Dao
interface ICurrentBoardingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(boardingQuizTable: BoardingQuizTable)

    @Update(entity = BoardingQuizTable::class)
    suspend fun update(boardingQuizTable: BoardingQuizTable)

    @Query("SELECT * FROM boardingQuizTable WHERE participantId = :participantId")
    suspend fun read(participantId: String): BoardingQuizTable?

    @Delete
    suspend fun delete(boardingQuizTable: BoardingQuizTable)

    @Query("DELETE FROM boardingQuizTable")
    suspend fun delete()
}