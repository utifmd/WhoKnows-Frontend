package com.dudegenuine.remote.service

import com.dudegenuine.model.request.LoginRequest
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.UserEntity
import com.dudegenuine.remote.service.contract.IUserService
import com.dudegenuine.remote.service.contract.IUserService.Companion.ACCEPT
import com.dudegenuine.remote.service.contract.IUserService.Companion.API_KEY
import com.dudegenuine.remote.service.contract.IUserService.Companion.CONTENT_TYPE
import com.dudegenuine.remote.service.contract.IUserService.Companion.ENDPOINT
import retrofit2.http.*

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
interface UserService: IUserService {

    @Headers(API_KEY, CONTENT_TYPE, ACCEPT)
    @POST(ENDPOINT)
    override suspend fun create(
        @Body entity: UserEntity): Response<UserEntity>

    @Headers(API_KEY, ACCEPT)
    @GET("${ENDPOINT}/{userId}")
    override suspend fun read(
        @Path("userId") id: String): Response<UserEntity>

    @Headers(API_KEY, CONTENT_TYPE, ACCEPT)
    @PUT("${ENDPOINT}/{userId}")
    override suspend fun update(
        @Path("userId") id: String,
        @Body entity: UserEntity): Response<UserEntity>

    @Headers(API_KEY, ACCEPT)
    @DELETE("${ENDPOINT}/{userId}")
    override suspend fun delete(
        @Path("userId") id: String)

    @Headers(API_KEY, ACCEPT)
    @GET(ENDPOINT)
    override suspend fun list(
        @Query("page") page: Int,
        @Query("size") size: Int): Response<List<UserEntity>>

    @Headers(API_KEY, CONTENT_TYPE, ACCEPT)
    @POST("/api/auth/sign-in")
    override suspend fun signIn(
        @Body loginRequest: LoginRequest): Response<UserEntity>
}