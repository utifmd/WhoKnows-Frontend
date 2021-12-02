package com.dudegenuine.remote.mapper

import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.User
import com.dudegenuine.remote.mapper.contract.IUserDataMapper
import kotlinx.coroutines.flow.Flow

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
class UserDataMapper: IUserDataMapper {
    override fun asEntity(user: com.dudegenuine.model.User): User {
        return User(
            id = user.id,
            fullName = user.fullName,
            username = user.username,
            phone = user.phone,
            email = user.email,
            password = user.password,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
    }

    override fun asUser(response: Response<User>): com.dudegenuine.model.User {
        return com.dudegenuine.model.User(
            id = response.data.id,
            fullName = response.data.fullName,
            username = response.data.username,
            phone = response.data.phone,
            email = response.data.email,
            password = response.data.password,
            createdAt = response.data.createdAt,
            updatedAt = response.data.updatedAt
        )
    }

    override fun asUsers(response: Response<List<User>>): List<com.dudegenuine.model.User> {
        return response.data.map {
            asUser(Response(response.code, response.status, it))
        }
    }
}