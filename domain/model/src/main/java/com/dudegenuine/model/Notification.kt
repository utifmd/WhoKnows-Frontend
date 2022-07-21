package com.dudegenuine.model

import com.dudegenuine.model.common.Utility.EMPTY_STRING
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
    val recipientIds: List<String>,
    val isDetail: Boolean = false,
    val title: String = EMPTY_STRING,
    val to: String = EMPTY_STRING,
    val imageUrl: String = EMPTY_STRING,
    val createdAt: Date,
    val updatedAt: Date?,
    val sender: User.Censored?){

    val isPropsBlank: Boolean = notificationId.isBlank() || userId.isBlank() ||
            roomId.isBlank() || event.isBlank() || recipientId.isBlank() || to.isBlank()
}
