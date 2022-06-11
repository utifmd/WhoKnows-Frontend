package com.dudegenuine.repository

import androidx.paging.PagingSource
import com.dudegenuine.model.Notification
import com.dudegenuine.model.ResourcePaging
import com.dudegenuine.remote.mapper.contract.INotificationDataMapper
import com.dudegenuine.remote.service.contract.INotificationService
import com.dudegenuine.repository.contract.INotificationRepository
import com.dudegenuine.repository.contract.dependency.local.IPreferenceManager
import javax.inject.Inject

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
class NotificationRepository
    @Inject constructor(
    private val service: INotificationService,
    private val mapper: INotificationDataMapper,
    private val prefs: IPreferenceManager
    ): INotificationRepository {

    override suspend fun create(notification: Notification): Notification {
        return mapper.asNotification(
            service.create(mapper.asEntity(notification)))
    }

    override suspend fun update(fresh: Notification): Notification {
        return mapper.asNotification(
            service.update(fresh.notificationId, mapper.asEntity(fresh))
        )
    }

    override suspend fun read(id: String): Notification {
        return mapper.asNotification(
            service.read(id))
    }

    override suspend fun delete(id: String)
        { service.delete(id) }

    override suspend fun delete(roomId: String, userId: String) {
        service.delete(roomId, userId)
    }

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

    override suspend fun listComplete(recipientId: String, page: Int, size: Int): List<Notification> {
        return mapper.asNotifications(
            service.pages(recipientId, page, size)
        )
    }

    override fun pages(recipientId: String, batchSize: Int): PagingSource<Int, Notification> = try {
        ResourcePaging { page -> listComplete(recipientId, page, batchSize) }
    } catch (e: Exception) {
        ResourcePaging { emptyList() }
    }


    /*override val currentUserId: () ->
        String = { prefs.readString(CURRENT_USER_ID) }

    override val currentBadge: () ->
        Int = { prefs.readInt(CURRENT_NOTIFICATION_BADGE) }

    override val onChangeCurrentBadge: (Int) ->
        Unit = { prefs.write(CURRENT_NOTIFICATION_BADGE, it) }

    override val registerPrefsListener: (
        SharedPreferences.OnSharedPreferenceChangeListener) -> Unit = prefs.register

    override val unregisterPrefsListener: (
        SharedPreferences.OnSharedPreferenceChangeListener) -> Unit = prefs.unregister*/
}