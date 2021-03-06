package com.dudegenuine.remote.service

import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.RoomEntity
import com.dudegenuine.remote.service.contract.IRoomService
import com.dudegenuine.remote.service.contract.IRoomService.Companion.ACCEPT
import com.dudegenuine.remote.service.contract.IRoomService.Companion.API_KEY
import com.dudegenuine.remote.service.contract.IRoomService.Companion.CONTENT_TYPE
import com.dudegenuine.remote.service.contract.IRoomService.Companion.ENDPOINT
import retrofit2.http.*

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface RoomService: IRoomService {
    @Headers(API_KEY, CONTENT_TYPE, ACCEPT)
    @POST(ENDPOINT)
    override suspend fun create(
        @Body entity: RoomEntity.Complete): Response<RoomEntity.Complete>

    @Headers(API_KEY, ACCEPT)
    @GET("${ENDPOINT}/{userId}")
    override suspend fun read(
        @Path("userId") id: String): Response<RoomEntity.Complete>

    @Headers(API_KEY, CONTENT_TYPE, ACCEPT)
    @PUT("${ENDPOINT}/{userId}")
    override suspend fun update(
        @Path("userId") id: String,
        @Body entity: RoomEntity.Complete): Response<RoomEntity.Complete>

    @Headers(API_KEY, ACCEPT)
    @DELETE("${ENDPOINT}/{userId}")
    override suspend fun delete(
        @Path("userId") id: String)

    @Headers(API_KEY, ACCEPT)
    @GET(ENDPOINT)
    override suspend fun listComplete(
        @Query("page") page: Int,
        @Query("size") size: Int): Response<List<RoomEntity.Complete>>

    @Headers(API_KEY, ACCEPT)
    @GET(ENDPOINT)
    override suspend fun listCensored(
        @Query("page") page: Int,
        @Query("size") size: Int): Response<List<RoomEntity.Censored>>

    @Headers(API_KEY, ACCEPT)
    @GET("${ENDPOINT}/owner/{userId}")
    override suspend fun listComplete(
        @Path("userId") userId: String,
        @Query("page") page: Int,
        @Query("size") size: Int): Response<List<RoomEntity.Complete>>

    @Headers(API_KEY, ACCEPT)
    @GET("${ENDPOINT}/search/{titleOrDesc}")
    override suspend fun listCensoredSearched(
        @Path("titleOrDesc") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int): Response<List<RoomEntity.Censored>>
}