package com.dudegenuine.remote.service

import com.dudegenuine.remote.entity.NotificationEntity
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.service.contract.INotificationService
import com.dudegenuine.remote.service.contract.INotificationService.Companion.ACCEPT
import com.dudegenuine.remote.service.contract.INotificationService.Companion.API_KEY
import com.dudegenuine.remote.service.contract.INotificationService.Companion.CONTENT_TYPE
import com.dudegenuine.remote.service.contract.INotificationService.Companion.ENDPOINT
import retrofit2.http.*

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/

interface NotificationService: INotificationService {
    @Headers(API_KEY, CONTENT_TYPE,  ACCEPT)
    @POST(ENDPOINT)
    override suspend fun create(
        @Body entity: NotificationEntity): Response<NotificationEntity>

    @Headers(API_KEY, ACCEPT)
    @GET("$ENDPOINT/{id}")
    override suspend fun read(
        @Path("id") id: String): Response<NotificationEntity>

    @Headers(API_KEY, CONTENT_TYPE,  ACCEPT)
    @GET("$ENDPOINT/{id}")
    override suspend fun update(
        @Path("id") id: String,
        @Body entity: NotificationEntity): Response<NotificationEntity>

    @Headers(API_KEY, ACCEPT)
    @DELETE("$ENDPOINT/{id}")
    override suspend fun delete(
        @Path("id") id: String)

    @Headers(API_KEY, ACCEPT)
    @GET(ENDPOINT)
    override suspend fun list(
        @Query("page") page: Int,
        @Query("size") size: Int): Response<List<NotificationEntity>>

    @Headers(API_KEY, ACCEPT)
    @GET("$ENDPOINT/owner/{recipientId}")
    override suspend fun list(
        @Path("recipientId") recipientId: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<List<NotificationEntity>>
}