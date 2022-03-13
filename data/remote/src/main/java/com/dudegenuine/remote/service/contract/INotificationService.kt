package com.dudegenuine.remote.service.contract

import com.dudegenuine.remote.entity.NotificationEntity
import com.dudegenuine.remote.entity.Response

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
interface INotificationService {
    suspend fun create(entity: NotificationEntity): Response<NotificationEntity>
    suspend fun read(id: String): Response<NotificationEntity>
    suspend fun update(id: String, entity: NotificationEntity): Response<NotificationEntity>
    suspend fun delete(id: String)
    suspend fun delete(roomId: String, userId: String)
    suspend fun list(page: Int, size: Int): Response<List<NotificationEntity>>
    suspend fun list(recipientId: String, page: Int, size: Int): Response<List<NotificationEntity>>

    companion object {
        const val API_KEY = "X-Api-Key: utif.pages.dev"
        const val CONTENT_TYPE = "Content-Type: application/json"
        const val ACCEPT = "Accept: application/json"
        const val ENDPOINT = "/api/notifications"
    }
}