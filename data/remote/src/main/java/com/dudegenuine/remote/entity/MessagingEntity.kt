package com.dudegenuine.remote.entity

import com.dudegenuine.model.Messaging
import com.google.gson.annotations.SerializedName

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
data class MessagingPushEntity(
    @SerializedName("data")
    val data: Messaging.Pusher,

    @SerializedName("to")
    val to: String
)

data class MessagingAddEntity(
    @SerializedName("operation")
    val operation: String,

    @SerializedName("notification_key_name")
    val notificationKeyName: String,

    @SerializedName("notification_key")
    val notificationKey: String,

    @SerializedName("registration_ids")
    val registrationIds: List<String>
)

data class MessagingCreateEntity(
    @SerializedName("operation")
    val operation: String,

    @SerializedName("notification_key_name")
    val notificationKeyName: String,

    @SerializedName("registration_ids")
    val registrationIds: List<String>
)
