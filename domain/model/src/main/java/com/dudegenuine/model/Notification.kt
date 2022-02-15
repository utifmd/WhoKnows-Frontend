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
    val seen: Boolean,
    val recipientId: String,
    val createdAt: Date,
    val updatedAt: Date?,
    val sender: UserCensored?){

    val isPropsBlank: Boolean = notificationId.isBlank() || userId.isBlank() ||
            roomId.isBlank() || event.isBlank() || recipientId.isBlank()
}
