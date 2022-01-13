package com.dudegenuine.repository.contract

import com.dudegenuine.local.entity.CurrentUser
import com.dudegenuine.model.User

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
interface IUserRepository {
    suspend fun create(user: User): User
    suspend fun read(id: String): User
    suspend fun update(id: String, user: User): User
    suspend fun delete(id: String)
    suspend fun list(page: Int, size: Int): List<User>
    suspend fun signIn(params: Map<String, String>): User

    suspend fun load(userId: String? = null): User?
    suspend fun save(currentUser: CurrentUser)
    suspend fun unload(userId: String)

    companion object {
        const val NOT_FOUND = "User not found."
    }
}