package com.dudegenuine.remote.mapper

import com.dudegenuine.model.Notification
import com.dudegenuine.model.PushNotification
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.remote.entity.FcmEntity
import com.dudegenuine.remote.entity.NotificationEntity
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.mapper.contract.INotificationDataMapper
import okhttp3.ResponseBody

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
class NotificationDataMapper: INotificationDataMapper {
    override fun asEntity(notification: Notification): NotificationEntity {
        return NotificationEntity(
            notificationId = notification.notificationId,
            userId = notification.userId,
            roomId = notification.roomId,
            event = notification.event,
            seen = notification.seen,
            recipientId = notification.recipientId,
            createdAt = notification.createdAt,
            updatedAt = notification.updatedAt,
            sender = notification.sender
        )
    }

    override fun asNotification(entity: NotificationEntity): Notification {
        return Notification(
            notificationId = entity.notificationId,
            userId = entity.userId,
            roomId = entity.roomId,
            event = entity.event,
            seen = entity.seen,
            recipientId = entity.recipientId,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            sender = entity.sender
        )
    }

    override fun asNotification(response: Response<NotificationEntity>): Notification {
        return when (response.data){
            is NotificationEntity -> asNotification(response.data)
            else -> throw IllegalStateException("invalid response")
        }
    }

    override fun asNotifications(response: Response<List<NotificationEntity>>): List<Notification> {
        return when (response.data){
            is List<*> -> {
                val list = response.data.filterIsInstance<NotificationEntity>()

                list.map { asNotification(it) }
            }
            else -> throw IllegalStateException("invalid response")
        }
    }

    override fun asPushNotification(entity: FcmEntity): PushNotification {
        return PushNotification(
            notificationId = entity.data.notificationId,
            topic = entity.to,
            userId = entity.data.userId,
            roomId = entity.data.roomId,
            event = entity.data.event,
            seen = entity.data.seen,
            recipientId = entity.data.recipientId,
            sender = entity.data.sender,
            senderProfileUrl = entity.data.senderProfileUrl,
            createdAt = entity.data.createdAt,
            updatedAt = entity.data.updatedAt,
        )
    }

    override fun asFcmSendEntity(notification: PushNotification): FcmEntity {
        return FcmEntity(
            to = notification.topic,
            data = notification
        )
    }

    override fun asResponseBody(response: retrofit2.Response<ResponseBody>): ResponseBody {
        if (!response.isSuccessful)
            throw HttpFailureException("response unsuccessful")

        val body = response.body() ?:
            throw HttpFailureException(response.errorBody()?.toString() ?: response.message())

        return body
    }
}