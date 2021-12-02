package com.dudegenuine.remote.mapper.contract

import com.dudegenuine.model.User
import com.dudegenuine.remote.entity.Response
import kotlinx.coroutines.flow.Flow

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
interface IUserDataMapper {
    fun asEntity(user: User): com.dudegenuine.remote.entity.User
    fun asUser(response: Response<com.dudegenuine.remote.entity.User>): User
    fun asUsers(response: Response<List<com.dudegenuine.remote.entity.User>>): List<User>
}