package com.dudegenuine.remote.service

import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.ResultEntity
import com.dudegenuine.remote.service.contract.IResultService
import com.dudegenuine.remote.service.contract.IResultService.Companion.ACCEPT
import com.dudegenuine.remote.service.contract.IResultService.Companion.API_KEY
import com.dudegenuine.remote.service.contract.IResultService.Companion.CONTENT_TYPE
import com.dudegenuine.remote.service.contract.IResultService.Companion.ENDPOINT
import retrofit2.http.*

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface ResultService: IResultService {
    @Headers(API_KEY, CONTENT_TYPE, ACCEPT)
    @POST(ENDPOINT)
    override suspend fun create(
        @Body entity: ResultEntity): Response<ResultEntity>

    @Headers(API_KEY, ACCEPT)
    @GET("${ENDPOINT}/{userId}")
    override suspend fun read(
        @Path("userId") id: String): Response<ResultEntity>

    @Headers(API_KEY, CONTENT_TYPE, ACCEPT)
    @PUT("${ENDPOINT}/{userId}")
    override suspend fun update(
        @Path("userId") id: String,
        @Body entity: ResultEntity): Response<ResultEntity>

    @Headers(API_KEY, ACCEPT)
    @DELETE("${ENDPOINT}/{userId}")
    override suspend fun delete(
        @Path("userId") id: String)

    @Headers(API_KEY, ACCEPT)
    @GET(ENDPOINT)
    override suspend fun list(
        @Query("page") page: Int,
        @Query("size") size: Int): Response<List<ResultEntity>>
}