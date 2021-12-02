package com.dudegenuine.repository.contract

import com.dudegenuine.model.User
import com.dudegenuine.model.request.LoginRequest

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

    suspend fun signIn(loginRequest: LoginRequest): User
}