package com.dudegenuine.model

import java.util.*

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
data class Notification(
    val notificationId: String,
    val userId: String,
    val roomId: String,
    val event: String,
    var seen: Boolean,
    val recipientId: String,
    val isDetail: Boolean = true,
    val createdAt: Date,
    val updatedAt: Date?,
    val sender: User.Censored?){

    val isPropsBlank: Boolean = notificationId.isBlank() || userId.isBlank() ||
            roomId.isBlank() || event.isBlank() || recipientId.isBlank()
}
