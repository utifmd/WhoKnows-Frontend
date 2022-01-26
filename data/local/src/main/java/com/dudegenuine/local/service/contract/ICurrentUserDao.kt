package com.dudegenuine.local.service.contract

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.dudegenuine.local.entity.CurrentUser

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
@Dao
interface ICurrentUserDao {
    @Insert(onConflict = REPLACE)
    suspend fun create(currentUser: CurrentUser)

    @Update(entity = CurrentUser::class)
    suspend fun update(currentUser: CurrentUser)

    @Query("SELECT * FROM currentUser WHERE userId = :userId")
    suspend fun read(userId: String): CurrentUser?

    @Query("SELECT * FROM currentUser")
    suspend fun list(): List<CurrentUser>

    @Delete
    suspend fun delete(currentUser: CurrentUser)
}