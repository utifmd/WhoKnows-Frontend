package com.dudegenuine.repository.contract

import androidx.paging.PagingSource
import com.dudegenuine.model.Notification
import com.dudegenuine.repository.contract.dependency.local.IIntentFactory
import com.dudegenuine.repository.contract.dependency.local.INotifyManager
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory
import com.dudegenuine.repository.contract.dependency.local.IResourceDependency

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
    suspend fun listComplete(recipientId: String, page: Int, size: Int): List<Notification>

    fun pages(recipientId: String, batchSize: Int): PagingSource<Int, Notification>

    val notifier: INotifyManager
    val resource: IResourceDependency
    val prefs: IPrefsFactory
    val intent: IIntentFactory
    val initial: Notification

    companion object {
        const val NOT_FOUND = "Notification not found."
        const val PAGE_SIZE = 5
    }
}