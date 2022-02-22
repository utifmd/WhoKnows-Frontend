package com.dudegenuine.local.service.contract

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.dudegenuine.local.entity.UserTable

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
@Dao
interface ICurrentUserDao {
    @Insert(onConflict = REPLACE)
    suspend fun create(userTable: UserTable)

    @Update(entity = UserTable::class)
    suspend fun update(userTable: UserTable)

    @Query("SELECT * FROM userTable WHERE userId = :userId")
    suspend fun read(userId: String): UserTable?

    @Query("SELECT * FROM userTable")
    suspend fun list(): List<UserTable>

    @Delete
    suspend fun delete(userTable: UserTable)

    @Query("DELETE FROM userTable")
    suspend fun delete()
}