package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.usecase.notification.DeleteNotification
import com.dudegenuine.usecase.notification.GetNotification
import com.dudegenuine.usecase.notification.GetNotifications
import com.dudegenuine.usecase.notification.PostNotification

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
interface INotificationUseCaseModule {
    val currentUserId: () -> String

    val postNotification: PostNotification
    val getNotification: GetNotification
    val deleteNotification: DeleteNotification
    val getNotifications: GetNotifications
}