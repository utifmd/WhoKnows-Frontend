package com.dudegenuine.remote.entity

import com.google.gson.annotations.SerializedName

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
data class FcmOperationResponse(

    @SerializedName("notificationKey")
    val notification_key: String
)

data class FcmPushResponse(

    @SerializedName("success")
    val success: Int,

    @SerializedName("failure")
    val failure: Int,

    @SerializedName("failed_registration_ids")
    val failedRegistrationIds: List<String>
)
