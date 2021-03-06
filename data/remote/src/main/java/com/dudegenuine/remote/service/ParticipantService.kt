package com.dudegenuine.remote.service

import com.dudegenuine.remote.entity.ParticipantEntity
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.service.contract.IParticipantService
import com.dudegenuine.remote.service.contract.IParticipantService.Companion.ACCEPT
import com.dudegenuine.remote.service.contract.IParticipantService.Companion.API_KEY
import com.dudegenuine.remote.service.contract.IParticipantService.Companion.CONTENT_TYPE
import com.dudegenuine.remote.service.contract.IParticipantService.Companion.ENDPOINT
import retrofit2.http.*

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface ParticipantService: IParticipantService {
    @Headers(API_KEY, CONTENT_TYPE, ACCEPT)
    @POST(ENDPOINT)
    override suspend fun create(
        @Body entity: ParticipantEntity): Response<ParticipantEntity>

    @Headers(API_KEY, ACCEPT)
    @GET("${ENDPOINT}/{participantId}")
    override suspend fun read(
        @Path("participantId") id: String): Response<ParticipantEntity>

    @Headers(API_KEY, ACCEPT)
    @GET("$ENDPOINT/owner/with_args")
    override suspend fun read(
        @Query("userId") userId: String,
        @Query("roomId") roomId: String): Response<ParticipantEntity>

    @Headers(API_KEY, CONTENT_TYPE, ACCEPT)
    @PUT("${ENDPOINT}/{participantId}")
    override suspend fun update(
        @Path("participantId") id: String,
        @Body entity: ParticipantEntity): Response<ParticipantEntity>

    @Headers(API_KEY, ACCEPT)
    @DELETE("${ENDPOINT}/{participantId}")
    override suspend fun delete(
        @Path("participantId") id: String)

    @Headers(API_KEY, ACCEPT)
    @GET(ENDPOINT)
    override suspend fun list(
        @Query("page") page: Int,
        @Query("size") size: Int): Response<List<ParticipantEntity>>
}