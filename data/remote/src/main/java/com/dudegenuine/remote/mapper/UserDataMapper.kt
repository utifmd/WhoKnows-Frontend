package com.dudegenuine.remote.mapper

import com.dudegenuine.local.entity.CurrentUser
import com.dudegenuine.model.User
import com.dudegenuine.model.User.Companion.PASSWORD
import com.dudegenuine.model.User.Companion.PAYLOAD
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.UserEntity
import com.dudegenuine.remote.mapper.contract.IUserDataMapper
import com.google.gson.Gson
import java.util.*
import javax.inject.Inject

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
class UserDataMapper
    @Inject constructor(val gson: Gson): IUserDataMapper {

    override fun asEntity(user: User): UserEntity {
        return UserEntity(
            userId = user.id,
            fullName = user.fullName,
            username = user.username,
            phone = user.phone,
            email = user.email,
            password = user.password,
            profileUrl = user.profileUrl,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
    }

    override fun asUser(entity: UserEntity): User {
        return User(
            id = entity.userId,
            fullName = entity.fullName,
            username = entity.username,
            phone = entity.phone,
            email = entity.email,
            password = entity.password,
            profileUrl = entity.profileUrl,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    override fun asUser(response: Response<UserEntity>): User {
        return when(response.data){
            is UserEntity -> asUser(response.data)
            else -> throw HttpFailureException(response.data.toString())
        }
    }

    override fun asUser(json: String): User {

        val adapter = gson.getAdapter(User::class.java)

        return adapter.fromJson(json)
    }

    override fun asUsers(response: Response<List<UserEntity>>): List<User> {
        val list = mutableListOf<User>()

        when(response.data){
            is List<*> -> response.data.filterIsInstance<UserEntity>().forEach {
                list.add(asUser(it))
            }
            else -> throw HttpFailureException(response.data.toString())
        }

        return list
    }

    override fun asLogin(params: Map<String, String>): UserEntity.LoginRequest {
        val payload = params[PAYLOAD] ?: throw HttpFailureException("incorrect payload")
        val password = params[PASSWORD] ?: throw HttpFailureException("incorrect password")

        return UserEntity.LoginRequest(
            payload, password
        )
    }

    override fun asUser(currentUser: CurrentUser): User {
        return currentUser.let { User(
            id = it.userId,
            fullName = it.fullName,
            email = it.email,
            phone = it.phone,
            username = it.username,
            password = it.password,
            profileUrl = it.profileUrl,
            createdAt = Date(it.createdAt),
            updatedAt = it.updatedAt?.let { date -> Date(date) }
        )}

        /*return null*/
    }

    override fun asCurrentUser(user: User): CurrentUser {
        return CurrentUser(
            userId = user.id,
            fullName = user.fullName,
            email = user.email,
            phone = user.phone,
            username = user.username,
            password = user.password,
            profileUrl = user.profileUrl,
            createdAt = user.createdAt.time,
            updatedAt = user.updatedAt?.time
        )
    }
}