package com.dudegenuine.repository

import com.dudegenuine.local.api.IPreferenceManager
import com.dudegenuine.local.api.IPreferenceManager.Companion.CURRENT_USER_ID
import com.dudegenuine.model.Notification
import com.dudegenuine.model.PushNotification
import com.dudegenuine.remote.mapper.contract.INotificationDataMapper
import com.dudegenuine.remote.service.contract.INotificationService
import com.dudegenuine.remote.service.contract.IPushNotificationService
import com.dudegenuine.repository.contract.INotificationRepository
import okhttp3.ResponseBody
import javax.inject.Inject

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
class NotificationRepository
    @Inject constructor(
        private val serviceOnPremise: INotificationService,
        private val serviceOnCloud: IPushNotificationService,
        private val mapper: INotificationDataMapper,
        private val prefs: IPreferenceManager): INotificationRepository {

    override suspend fun create(notification: Notification): Notification {
        return mapper.asNotification(
            serviceOnPremise.create(mapper.asEntity(notification)))
    }

    override suspend fun push(notification: PushNotification): ResponseBody {
        return mapper.asResponseBody(
            serviceOnCloud.post(mapper.asFcmSendEntity(notification)))
    }

    override suspend fun read(id: String): Notification {
        return mapper.asNotification(
            serviceOnPremise.read(id))
    }

    override suspend fun delete(id: String)
        { serviceOnPremise.delete(id) }

    override suspend fun list(page: Int, size: Int): List<Notification> {
        return mapper.asNotifications(
            serviceOnPremise.list(page, size)
        )
    }

    override suspend fun list(recipientId: String, page: Int, size: Int): List<Notification> {
        return mapper.asNotifications(
            serviceOnPremise.list(recipientId, page, size)
        )
    }

    override val currentUserId: () -> String =
        { prefs.read(CURRENT_USER_ID) }
}