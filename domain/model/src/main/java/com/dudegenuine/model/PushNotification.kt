package com.dudegenuine.model

import java.util.*

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
data class PushNotification(
    val notificationId: String,
    val topic: String,
    val userId: String,
    val roomId: String,
    val event: String,
    val seen: Boolean,
    val recipientId: String,
    val sender: String,
    val senderProfileUrl: String,
    val createdAt: Date,
    val updatedAt: Date?
)
