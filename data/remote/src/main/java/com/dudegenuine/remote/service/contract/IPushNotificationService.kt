package com.dudegenuine.remote.service.contract

import com.dudegenuine.remote.entity.FcmEntity
import okhttp3.ResponseBody
import retrofit2.Response

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
interface IPushNotificationService {
    fun post(entity: FcmEntity): Response<ResponseBody>

    companion object {
        const val TOPIC = "/topics/"
        const val ENDPOINT_FCM_SEND = "/fcm/send"
        const val ENDPOINT_FCM_NOTIFICATION = "/fcm/notification"
        const val AUTH_KEY_FCM = "Authorization: key=AAAAfd49hCg:APA91bEENFcJbmG1JAKM_KRoCVmimDpWXPxExfZ1Y6mG3gsy8nIyL3Z-eepeP6iBh--ZOI_tX4Z7nsKyxqWV9fQMIkDkiBS07Oyb8_0hvZpc4WAjynfrFlmyVYeyLdqYz1BgRWR7krC8"
        const val CONTENT_TYPE = "application/json"
    }
}