package com.dudegenuine.remote.mapper

import com.dudegenuine.model.Notification
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.remote.entity.NotificationEntity
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.mapper.contract.INotificationDataMapper
import com.dudegenuine.remote.mapper.contract.IUserDataMapper
import okhttp3.ResponseBody
import javax.inject.Inject

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
class NotificationDataMapper
    @Inject constructor(
    private val mapperUser: IUserDataMapper): INotificationDataMapper {

    override fun asEntity(notification: Notification): NotificationEntity {
        return NotificationEntity(
            notificationId = notification.notificationId,
            userId = notification.userId,
            roomId = notification.roomId,
            event = notification.event,
            seen = notification.seen,
            recipientId = notification.recipientId,
            recipientIds = notification.recipientIds,
            createdAt = notification.createdAt,
            updatedAt = notification.updatedAt,
            sender = notification.sender?.
                let(mapperUser::asUserCensoredEntity)

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
            recipientIds = entity.recipientIds,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            sender = entity.sender?.
                let(mapperUser::asUserCensored)
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

    override fun asResponseBody(response: retrofit2.Response<ResponseBody>): ResponseBody {
        if (!response.isSuccessful) throw HttpFailureException("response unsuccessful")

        return response.body() ?: throw HttpFailureException(
            response.errorBody()?.toString() ?: response.message()
        )
    }
}