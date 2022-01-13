package com.dudegenuine.local.service

import com.dudegenuine.local.service.contract.ICurrentUserDao

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
/*@Dao*/
interface CurrentUserDao: ICurrentUserDao {

    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(currentUser: CurrentUser)

    @Query("SELECT * FROM currentUser WHERE userId = :userId")
    suspend fun read(userId: String): CurrentUser?

    @Delete
    suspend fun delete(currentUser: CurrentUser)*/
}