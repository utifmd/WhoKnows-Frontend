package com.dudegenuine.remote.service

import com.dudegenuine.remote.entity.ImpressionEntity
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.service.contract.IImpressionService
import com.dudegenuine.remote.service.contract.IImpressionService.Companion.ACCEPT
import com.dudegenuine.remote.service.contract.IImpressionService.Companion.API_KEY
import com.dudegenuine.remote.service.contract.IImpressionService.Companion.CONTENT_TYPE
import com.dudegenuine.remote.service.contract.IImpressionService.Companion.ENDPOINT
import retrofit2.http.*

/**
 * Wed, 06 Jul 2022
 * WhoKnows by utifmd
 **/
interface ImpressionService: IImpressionService {
    @Headers(API_KEY, CONTENT_TYPE, ACCEPT)
    @POST(ENDPOINT)
    override suspend fun create(
        @Body entity: ImpressionEntity): Response<ImpressionEntity>

    @Headers(API_KEY, CONTENT_TYPE, ACCEPT)
    @PUT("$ENDPOINT/{userId}")
    override suspend fun update(
        @Path("userId") id: String,
        @Body entity: ImpressionEntity): Response<ImpressionEntity>
}