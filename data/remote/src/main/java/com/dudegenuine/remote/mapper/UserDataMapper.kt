package com.dudegenuine.remote.mapper

import com.dudegenuine.model.validation.HttpFailureException
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.UserEntity
import com.dudegenuine.remote.mapper.contract.IUserDataMapper

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
class UserDataMapper: IUserDataMapper {
    override fun asEntity(user: com.dudegenuine.model.User): UserEntity {
        return UserEntity(
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

    override fun asUser(entity: UserEntity): com.dudegenuine.model.User {
        return com.dudegenuine.model.User(
            id = entity.id,
            fullName = entity.fullName,
            username = entity.username,
            phone = entity.phone,
            email = entity.email,
            password = entity.password,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    override fun asUser(response: Response<Any>): com.dudegenuine.model.User {
        return when(response.data){
            is UserEntity -> asUser(response.data)
            else -> throw HttpFailureException(response.data.toString())
        }
    }

    override fun asUsers(response: Response<Any>): List<com.dudegenuine.model.User> {
        val list = mutableListOf<com.dudegenuine.model.User>()
        when(response.data){
            is List<*> -> response.data.filterIsInstance<UserEntity>().forEach {
                list.add(asUser(it))
            }
            else -> throw HttpFailureException(response.data.toString())
        }
        return list
    }
}