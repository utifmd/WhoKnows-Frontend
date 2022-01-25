package com.dudegenuine.remote.mapper.contract

import com.dudegenuine.local.entity.CurrentUser
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
    fun asUser(json: String): User
    fun asUsers(response: Response<List<UserEntity>>): List<User>
    fun asLogin(params: Map<String, String>): UserEntity.LoginRequest

    /*fun asUserOrNull(currentUser: CurrentUser?): User?*/
    fun asCurrentUser(user: User): CurrentUser
    fun asUser(currentUser: CurrentUser): User
}