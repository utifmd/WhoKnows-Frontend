package com.dudegenuine.whoknows.ui.vm.notification.contract

import com.dudegenuine.model.Notification

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
interface INotificationViewModel {
    fun postNotification(notification: Notification)
    fun getNotification(id: String)
    fun deleteNotification(id: String)
    fun getNotifications(page: Int, size: Int)
    fun getNotifications(recipientId: String, page: Int, size: Int)
}