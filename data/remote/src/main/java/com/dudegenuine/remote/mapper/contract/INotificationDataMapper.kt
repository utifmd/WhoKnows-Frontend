package com.dudegenuine.remote.mapper.contract

import com.dudegenuine.model.Notification
import com.dudegenuine.model.PushNotification
import com.dudegenuine.remote.entity.NotificationEntity
import com.dudegenuine.remote.entity.FcmEntity
import com.dudegenuine.remote.entity.Response
import okhttp3.ResponseBody

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
interface INotificationDataMapper {
    fun asEntity(notification: Notification): NotificationEntity
    fun asNotification(entity: NotificationEntity): Notification
    fun asNotification(response: Response<NotificationEntity>): Notification
    fun asNotifications(response: Response<List<NotificationEntity>>): List<Notification>
    fun asPushNotification(entity: FcmEntity): PushNotification
    fun asFcmSendEntity(notification: PushNotification): FcmEntity
    fun asResponseBody(response: retrofit2.Response<ResponseBody>): ResponseBody
}