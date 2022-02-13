package com.dudegenuine.remote.entity

import com.dudegenuine.model.PushNotification
import com.google.gson.annotations.SerializedName

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
data class FcmEntity(
    @SerializedName("data")
    val data: PushNotification,

    @SerializedName("to")
    val to: String
)

data class FcmCaseEntity(
    @SerializedName("operation")
    val operation: String,

    @SerializedName("notification_key_name")
    val notificationKeyName: String,

    @SerializedName("registration_ids")
    val registrationIds: List<String>,

    @SerializedName("notification_key")
    val notificationKey: String?
)

