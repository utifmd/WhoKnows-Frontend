package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.contract.INotificationRepository
import com.dudegenuine.usecase.notification.*
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.INotificationUseCaseModule

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
class NotificationUseCaseModule(
    repository: INotificationRepository): INotificationUseCaseModule {

    override val currentUserId: () -> String = repository.currentUserId
    override val postNotification: PostNotification = PostNotification(repository)
    override val patchNotification: PatchNotification = PatchNotification(repository)
    override val getNotification: GetNotification = GetNotification(repository)
    override val deleteNotification: DeleteNotification = DeleteNotification(repository)
    override val getNotifications: GetNotifications = GetNotifications(repository)
    override val currentBadge: () -> String = repository.currentBadge
    override val onCurrentBadgeChange: (String) -> Unit = repository.onCurrentBadgeChange
}