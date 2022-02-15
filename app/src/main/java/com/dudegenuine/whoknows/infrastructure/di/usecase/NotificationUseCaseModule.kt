package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.contract.INotificationRepository
import com.dudegenuine.usecase.notification.DeleteNotification
import com.dudegenuine.usecase.notification.GetNotification
import com.dudegenuine.usecase.notification.GetNotifications
import com.dudegenuine.usecase.notification.PostNotification
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.INotificationUseCaseModule

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
class NotificationUseCaseModule(
    repository: INotificationRepository): INotificationUseCaseModule {

    override val currentUserId: () -> String = repository.currentUserId
    override val postNotification: PostNotification = PostNotification(repository)
    override val getNotification: GetNotification = GetNotification(repository)
    override val deleteNotification: DeleteNotification = DeleteNotification(repository)
    override val getNotifications: GetNotifications = GetNotifications(repository)
}