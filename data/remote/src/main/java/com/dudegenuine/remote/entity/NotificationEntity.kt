package com.dudegenuine.remote.entity

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
data class NotificationEntity(
    @SerializedName("notificationId")
    val notificationId: String,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("roomId")
    val roomId: String,

    @SerializedName("event")
    val event: String,

    @SerializedName("seen")
    val seen: Boolean,

    @SerializedName("recipientId")
    val recipientId: String,

    @SerializedName("recipientIds")
    val recipientIds: List<String>,

    @SerializedName("createdAt")
    val createdAt: Date,

    @SerializedName("updatedAt")
    val updatedAt: Date?,

    @SerializedName("sender")
    val sender: UserEntity.Censored?
)
