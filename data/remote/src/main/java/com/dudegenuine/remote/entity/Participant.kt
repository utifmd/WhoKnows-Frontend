package com.dudegenuine.remote.entity

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
data class Participant(

    @SerializedName("participantId")
    val id: String,

    @SerializedName("roomId")
    val roomId: String,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("currentPage")
    val currentPage: String,

    @SerializedName("timeLeft")
    val timeLeft: Int?,

    @SerializedName("expired")
    val expired: Boolean,

    @SerializedName("createdAt")
    val createdAt: Date,

    @SerializedName("updatedAt")
    val updatedAt: Date?,

    @SerializedName("results")
    val results: List<Result>
)
