package com.dudegenuine.remote.service

import com.dudegenuine.remote.entity.MessagingAddEntity
import com.dudegenuine.remote.entity.MessagingCreateEntity
import com.dudegenuine.remote.entity.MessagingPushEntity
import com.dudegenuine.remote.service.contract.IMessagingService
import com.dudegenuine.remote.service.contract.IMessagingService.Companion.AUTH_KEY_FCM
import com.dudegenuine.remote.service.contract.IMessagingService.Companion.CONTENT_TYPE
import com.dudegenuine.remote.service.contract.IMessagingService.Companion.ENDPOINT_FCM_NOTIFICATION
import com.dudegenuine.remote.service.contract.IMessagingService.Companion.ENDPOINT_FCM_SEND
import com.dudegenuine.remote.service.contract.IMessagingService.Companion.PROJECT_ID
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
interface MessagingService: IMessagingService {
    @Headers(AUTH_KEY_FCM, CONTENT_TYPE, PROJECT_ID)
    @GET(ENDPOINT_FCM_NOTIFICATION)
    override suspend fun get(
        @Query("notification_key_name") keyName: String): Response<ResponseBody>

    @Headers(AUTH_KEY_FCM, CONTENT_TYPE, PROJECT_ID)
    @POST(ENDPOINT_FCM_NOTIFICATION)
    override suspend fun create(
        @Body entity: MessagingCreateEntity): Response<ResponseBody>

    @Headers(AUTH_KEY_FCM, CONTENT_TYPE, PROJECT_ID)
    @POST(ENDPOINT_FCM_NOTIFICATION)
    override suspend fun add(
        @Body entity: MessagingAddEntity): Response<ResponseBody>

    @Headers(AUTH_KEY_FCM, CONTENT_TYPE)
    @POST(ENDPOINT_FCM_SEND)
    override suspend fun push(
        @Body entity: MessagingPushEntity): Response<ResponseBody>
}