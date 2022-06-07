package com.dudegenuine.local.service

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.dudegenuine.local.entity.UserTable

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
@Dao
interface IUsersDao {
    @Insert(onConflict = REPLACE)
    suspend fun create(userTable: UserTable)

    @Update(entity = UserTable::class)
    suspend fun update(userTable: UserTable)

    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun read(userId: String): UserTable?

    @Query("SELECT * FROM users")
    suspend fun list(): List<UserTable>

    @Delete
    suspend fun delete(userTable: UserTable)

    @Query("DELETE FROM users")
    suspend fun delete()
}