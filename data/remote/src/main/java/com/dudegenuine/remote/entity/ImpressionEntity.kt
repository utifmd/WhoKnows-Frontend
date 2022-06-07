package com.dudegenuine.remote.entity

import com.google.gson.annotations.SerializedName

/**
 * Mon, 30 May 2022
 * WhoKnows by utifmd
 **/
data class ImpressionEntity(
    @SerializedName("impressionId")
    val impressionId: String,

    @SerializedName("postId")
    val postId: String,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("good")
    val good: Boolean
)
