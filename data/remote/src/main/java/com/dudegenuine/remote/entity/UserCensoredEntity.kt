package com.dudegenuine.remote.entity

import com.google.gson.annotations.SerializedName

/**
 * Tue, 15 Feb 2022
 * WhoKnows by utifmd
 **/
data class UserCensoredEntity(
    @SerializedName("userId")
    val userId: String,

    @SerializedName("fullName")
    val fullName: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("profileUrl")
    val profileUrl: String,
)
