package com.dudegenuine.repository

import com.dudegenuine.local.api.IPreferenceManager
import com.dudegenuine.local.api.IPreferenceManager.Companion.CURRENT_USER_ID
import com.dudegenuine.model.Notification
import com.dudegenuine.remote.mapper.contract.INotificationDataMapper
import com.dudegenuine.remote.service.contract.INotificationService
import com.dudegenuine.repository.contract.INotificationRepository
import javax.inject.Inject

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
class NotificationRepository
    @Inject constructor(
        private val service: INotificationService,
        private val mapper: INotificationDataMapper,
        private val prefs: IPreferenceManager): INotificationRepository {

    override suspend fun create(notification: Notification): Notification {
        return mapper.asNotification(
            service.create(mapper.asEntity(notification)))
    }

    override suspend fun read(id: String): Notification {
        return mapper.asNotification(
            service.read(id))
    }

    override suspend fun delete(id: String)
        { service.delete(id) }

    override suspend fun list(page: Int, size: Int): List<Notification> {
        return mapper.asNotifications(
            service.list(page, size)
        )
    }

    override suspend fun list(recipientId: String, page: Int, size: Int): List<Notification> {
        return mapper.asNotifications(
            service.list(recipientId, page, size)
        )
    }

    override val currentUserId: () -> String =
        { prefs.read(CURRENT_USER_ID) }
}