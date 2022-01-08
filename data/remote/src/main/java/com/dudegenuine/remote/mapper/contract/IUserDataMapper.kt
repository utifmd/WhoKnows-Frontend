package com.dudegenuine.remote.mapper.contract

import com.dudegenuine.model.User
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.UserEntity

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
interface IUserDataMapper {
    fun asEntity(user: User): UserEntity
    fun asUser(entity: UserEntity): User
    fun asUser(response: Response<UserEntity>): User
    fun asUsers(response: Response<List<UserEntity>>): List<User>
    fun asLogin(params: Map<String, String>): UserEntity.LoginRequest
}