package com.dudegenuine.remote.service

import com.dudegenuine.remote.entity.FcmEntity
import com.dudegenuine.remote.service.contract.IPushNotificationService
import com.dudegenuine.remote.service.contract.IPushNotificationService.Companion.AUTH_KEY_FCM
import com.dudegenuine.remote.service.contract.IPushNotificationService.Companion.CONTENT_TYPE
import com.dudegenuine.remote.service.contract.IPushNotificationService.Companion.ENDPOINT_FCM_SEND
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
interface PushNotificationService: IPushNotificationService {
    @Headers(AUTH_KEY_FCM, CONTENT_TYPE)
    @POST(ENDPOINT_FCM_SEND)
    override fun post(
        @Body entity: FcmEntity): Response<ResponseBody>
}