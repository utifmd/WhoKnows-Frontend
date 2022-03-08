package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.usecase.notification.*

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
interface INotificationUseCaseModule {
    val currentUserId: () -> String

    val postNotification: PostNotification
    val patchNotification: PatchNotification
    val getNotification: GetNotification
    val deleteNotification: DeleteNotification
    val getNotifications: GetNotifications
}