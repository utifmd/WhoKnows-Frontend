package com.dudegenuine.local.service

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dudegenuine.local.entity.CurrentUser
import com.dudegenuine.local.service.contract.ICurrentUserDao

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
@Dao
interface CurrentUserDao: ICurrentUserDao {

    @Insert
    override suspend fun create(currentUser: CurrentUser)

    @Query("SELECT * FROM currentUser WHERE userId = :userId")
    override suspend fun read(userId: String): CurrentUser?

    @Delete
    override suspend fun delete(currentUser: CurrentUser)
}