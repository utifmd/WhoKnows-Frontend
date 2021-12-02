package com.dudegenuine.remote.entity

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
data class Result (

    @SerializedName("resultId")
    val id: String,

    @SerializedName("participantId")
    val participantId: String,

    @SerializedName("roomId")
    val roomId: String,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("correctQuiz")
    val correctQuiz: List<String>,

    @SerializedName("wrongQuiz")
    val wrongQuiz: List<String>,

    @SerializedName("score")
    val score: Int?,

    @SerializedName("createdAt")
    val createdAt: Date,

    @SerializedName("updatedAt")
    val updatedAt: Date?
)