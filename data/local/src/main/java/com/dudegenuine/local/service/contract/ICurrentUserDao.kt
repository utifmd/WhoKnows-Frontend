package com.dudegenuine.local.service.contract

import com.dudegenuine.local.entity.CurrentUser

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
interface ICurrentUserDao {
    suspend fun create(currentUser: CurrentUser)
    suspend fun read(userId: String): CurrentUser?
    suspend fun delete(currentUser: CurrentUser)
}