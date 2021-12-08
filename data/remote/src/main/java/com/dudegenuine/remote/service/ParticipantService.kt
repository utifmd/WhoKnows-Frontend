package com.dudegenuine.remote.service

import com.dudegenuine.remote.entity.ParticipantEntity
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.service.contract.IParticipantService
import com.dudegenuine.remote.service.contract.IParticipantService.Companion.ENDPOINT
import com.dudegenuine.remote.service.contract.IParticipantService.Companion.HEADER
import retrofit2.http.*

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface ParticipantService: IParticipantService {
    @Headers(HEADER)
    @POST(ENDPOINT)
    override suspend fun create(
        @Body entity: ParticipantEntity): Response<ParticipantEntity>

    @Headers(HEADER)
    @GET("${ENDPOINT}/{userId}")
    override suspend fun read(
        @Path("userId") id: String): Response<ParticipantEntity>

    @Headers(HEADER)
    @PATCH("${ENDPOINT}/{userId}")
    override suspend fun update(
        @Path("userId") id: String,
        @Body entity: ParticipantEntity): Response<ParticipantEntity>

    @Headers(HEADER)
    @DELETE("${ENDPOINT}/{userId}")
    override suspend fun delete(
        @Path("userId") id: String)

    @Headers(HEADER)
    @GET(ENDPOINT)
    override suspend fun list(
        @Query("page") page: Int,
        @Query("size") size: Int): Response<List<ParticipantEntity>>
}