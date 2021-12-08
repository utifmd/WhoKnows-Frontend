package com.dudegenuine.remote.service.contract

import com.dudegenuine.model.request.LoginRequest
import com.dudegenuine.model.validation.HttpFailureException
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.UserEntity
import retrofit2.HttpException
import java.io.IOException

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
interface IUserService {
    suspend fun create(entity: UserEntity): Response<UserEntity>
    suspend fun read(id: String): Response<UserEntity>
    suspend fun update(id: String, entity: UserEntity): Response<UserEntity>
    suspend fun delete(id: String)
    suspend fun list(page: Int, size: Int): Response<List<UserEntity>>
    suspend fun signIn(loginRequest: LoginRequest): Response<UserEntity>

    companion object {
        const val HEADER = "X-Api-Key: utif.pages.dev"
        const val ENDPOINT = "/api/users"
    }
}