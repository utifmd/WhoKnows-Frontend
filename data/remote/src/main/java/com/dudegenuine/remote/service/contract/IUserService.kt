package com.dudegenuine.remote.service.contract

import com.dudegenuine.model.User
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.UserEntity

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
interface IUserService {
    suspend fun create(entity: UserEntity.Complete): Response<UserEntity.Complete>
    suspend fun read(id: String): Response<UserEntity.Complete>
    suspend fun update(id: String, entity: UserEntity.Complete): Response<UserEntity.Complete>
    suspend fun delete(id: String)
    suspend fun list(page: Int, size: Int): Response<List<UserEntity.Complete>>
    suspend fun listOrderByParticipant(page: Int, size: Int): Response<List<UserEntity.Censored>>
    suspend fun signIn(loginRequest: User.Signer): Response<UserEntity.Complete>

    companion object {
        const val API_KEY = "X-Api-Key: utif.pages.dev"
        const val CONTENT_TYPE = "Content-Type: application/json"
        const val ACCEPT = "Accept: application/json"
        const val ENDPOINT = "/api/users"
    }
}