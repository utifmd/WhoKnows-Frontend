package com.dudegenuine.remote.service.contract

import com.dudegenuine.model.request.LoginRequest
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.UserEntity

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
interface IUserService {
    suspend fun create(entity: UserEntity): Response<Any>
    suspend fun read(id: String): Response<Any>
    suspend fun update(id: String, entity: UserEntity): Response<Any>
    suspend fun delete(id: String)
    suspend fun list(page: Int, size: Int): Response<Any>
    suspend fun signIn(loginRequest: LoginRequest): Response<Any>
}