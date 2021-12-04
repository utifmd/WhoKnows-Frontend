package com.dudegenuine.remote.service

import com.dudegenuine.model.request.LoginRequest
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.UserEntity
import com.dudegenuine.remote.service.contract.IUserService
import retrofit2.http.*

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
interface UserService: IUserService {

    @POST("/api/users")
    @Headers("X-Api-Key: utif.pages.dev")
    override suspend fun create(
        @Body entity: UserEntity): Response<Any>

    @GET("/api/users/{userId}")
    @Headers("X-Api-Key: utif.pages.dev")
    override suspend fun read(
        @Path("userId") id: String): Response<Any>

    @PUT("/api/users/{userId}")
    @Headers("X-Api-Key: utif.pages.dev")
    override suspend fun update(
        @Path("userId") id: String,
        @Body entity: UserEntity): Response<Any>

    @DELETE("/api/users/{userId}")
    @Headers("X-Api-Key: utif.pages.dev")
    override suspend fun delete(
        @Path("userId") id: String)

    @GET("/api/users")
    @Headers("X-Api-Key: utif.pages.dev")
    override suspend fun list(
        @Query("page") page: Int,
        @Query("size") size: Int): Response<Any>

    @POST("/api/auth/sign-in")
    @Headers("X-Api-Key: utif.pages.dev")
    override suspend fun signIn(
        @Body loginRequest: LoginRequest): Response<Any>
}