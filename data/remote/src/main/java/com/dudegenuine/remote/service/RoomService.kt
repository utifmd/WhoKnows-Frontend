package com.dudegenuine.remote.service

import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.RoomEntity
import com.dudegenuine.remote.service.contract.IRoomService
import com.dudegenuine.remote.service.contract.IRoomService.Companion.ENDPOINT
import com.dudegenuine.remote.service.contract.IRoomService.Companion.HEADER
import retrofit2.http.*

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface RoomService: IRoomService {
    @Headers(HEADER)
    @POST(ENDPOINT)
    override suspend fun create(
        @Body entity: RoomEntity): Response<RoomEntity>

    @Headers(HEADER)
    @GET("${ENDPOINT}/{userId}")
    override suspend fun read(
        @Path("userId") id: String): Response<RoomEntity>

    @Headers(HEADER)
    @PATCH("${ENDPOINT}/{userId}")
    override suspend fun update(
        @Path("userId") id: String,
        @Body entity: RoomEntity): Response<RoomEntity>

    @Headers(HEADER)
    @DELETE("${ENDPOINT}/{userId}")
    override suspend fun delete(
        @Path("userId") id: String)

    @Headers(HEADER)
    @GET(ENDPOINT)
    override suspend fun list(
        @Query("page") page: Int,
        @Query("size") size: Int): Response<List<RoomEntity>>
}