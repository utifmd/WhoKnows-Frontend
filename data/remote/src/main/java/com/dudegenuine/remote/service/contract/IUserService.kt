package com.dudegenuine.remote.service.contract

import com.dudegenuine.model.request.LoginRequest
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.User

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
interface IUserService {
    suspend fun create(user: User): Response<User>
    suspend fun read(id: String): Response<User>
    suspend fun update(id: String, user: User): Response<User>
    suspend fun delete(id: String)
    suspend fun list(page: Int, size: Int): Response<List<User>>

    suspend fun signIn(loginRequest: LoginRequest): Response<User>
}