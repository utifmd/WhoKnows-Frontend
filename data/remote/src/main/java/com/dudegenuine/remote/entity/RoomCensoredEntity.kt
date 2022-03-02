package com.dudegenuine.remote.entity

import com.google.gson.annotations.SerializedName

/**
 * Tue, 01 Mar 2022
 * WhoKnows by utifmd
 **/
data class RoomCensoredEntity(
    @SerializedName("roomId")
    val roomId: String,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("minute")
    val minute: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("expired")
    val expired: Boolean
)
