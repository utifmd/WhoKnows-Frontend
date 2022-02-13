package com.dudegenuine.repository.contract

import com.dudegenuine.model.Notification
import com.dudegenuine.model.PushNotification
import okhttp3.ResponseBody

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
interface INotificationRepository {
    suspend fun create(notification: Notification): Notification
    suspend fun push(notification: PushNotification): ResponseBody
    suspend fun read(id: String): Notification
    suspend fun delete(id: String)
    suspend fun list(page: Int, size: Int): List<Notification>
    suspend fun list(recipientId: String, page: Int, size: Int): List<Notification>

    val currentUserId: () -> String

    companion object {
        const val NOT_FOUND = "Notification not found."
    }
}