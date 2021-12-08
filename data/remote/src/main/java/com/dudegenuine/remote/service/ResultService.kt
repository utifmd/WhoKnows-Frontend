package com.dudegenuine.remote.service

import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.ResultEntity
import com.dudegenuine.remote.service.contract.IResultService
import com.dudegenuine.remote.service.contract.IResultService.Companion.ENDPOINT
import com.dudegenuine.remote.service.contract.IResultService.Companion.HEADER
import retrofit2.http.*

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface ResultService: IResultService {
    @Headers(HEADER)
    @POST(ENDPOINT)
    override suspend fun create(
        @Body entity: ResultEntity): Response<ResultEntity>

    @Headers(HEADER)
    @GET("${ENDPOINT}/{userId}")
    override suspend fun read(
        @Path("userId") id: String): Response<ResultEntity>

    @Headers(HEADER)
    @PATCH("${ENDPOINT}/{userId}")
    override suspend fun update(
        @Path("userId") id: String,
        @Body entity: ResultEntity): Response<ResultEntity>

    @Headers(HEADER)
    @DELETE("${ENDPOINT}/{userId}")
    override suspend fun delete(
        @Path("userId") id: String)

    @Headers(HEADER)
    @GET(ENDPOINT)
    override suspend fun list(
        @Query("page") page: Int,
        @Query("size") size: Int): Response<List<ResultEntity>>
}