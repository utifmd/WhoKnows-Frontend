package com.dudegenuine.remote.service.contract

import com.dudegenuine.remote.entity.MessagingAddEntity
import com.dudegenuine.remote.entity.MessagingCreateEntity
import com.dudegenuine.remote.entity.MessagingPushEntity
import com.dudegenuine.remote.entity.MessagingRemoveEntity
import okhttp3.ResponseBody
import retrofit2.Response

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
interface IMessagingService {
    suspend fun get(keyName: String): Response<ResponseBody>
    suspend fun create(entity: MessagingCreateEntity): Response<ResponseBody>
    suspend fun add(entity: MessagingAddEntity): Response<ResponseBody>
    suspend fun remove(entity: MessagingRemoveEntity): Response<ResponseBody>
    suspend fun push(entity: MessagingPushEntity): Response<ResponseBody>

    companion object {
        const val ENDPOINT_FCM_SEND = "/fcm/send"
        const val ENDPOINT_FCM_NOTIFICATION = "/fcm/notification"
        const val AUTH_KEY_FCM = "Authorization: key=AAAAfd49hCg:APA91bEENFcJbmG1JAKM_KRoCVmimDpWXPxExfZ1Y6mG3gsy8nIyL3Z-eepeP6iBh--ZOI_tX4Z7nsKyxqWV9fQMIkDkiBS07Oyb8_0hvZpc4WAjynfrFlmyVYeyLdqYz1BgRWR7krC8"
        const val CONTENT_TYPE = "Content-Type: application/json"
        const val PROJECT_ID = "project_id: 540599485480"
    }
}