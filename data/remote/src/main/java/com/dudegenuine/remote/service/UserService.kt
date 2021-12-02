package com.dudegenuine.remote.service

import com.dudegenuine.model.request.LoginRequest
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.User
import com.dudegenuine.remote.service.contract.IUserService
import retrofit2.http.*

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
abstract class UserService: IUserService {

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/api/users")
    abstract override suspend fun create(
        @Body user: User
    ): Response<User>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("/api/users/{userId}")
    abstract override suspend fun read(
        @Path("userId") id: String
    ): Response<User>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @PUT("/api/users/{userId}")
    abstract override suspend fun update(
        @Path("userId") id: String,
        @Body user: User
    ): Response<User>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @DELETE("/api/users/{userId}")
    abstract override suspend fun delete(
        @Path("userId") id: String
    )

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("/api/users")
    abstract override suspend fun list(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<List<User>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/api/auth/sign-in")
    abstract override suspend fun signIn(
        @Body loginRequest: LoginRequest
    ): Response<User>
}