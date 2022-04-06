package com.dudegenuine.repository.contract

import com.dudegenuine.model.Notification

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
interface INotificationRepository {
    suspend fun create(notification: Notification): Notification
    suspend fun update(fresh: Notification): Notification
    suspend fun read(id: String): Notification
    suspend fun delete(id: String)
    suspend fun delete(roomId: String, userId: String)
    suspend fun list(page: Int, size: Int): List<Notification>
    suspend fun list(recipientId: String, page: Int, size: Int): List<Notification>
    companion object {
        const val NOT_FOUND = "Notification not found."
    }
}