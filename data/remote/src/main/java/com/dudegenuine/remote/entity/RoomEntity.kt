package com.dudegenuine.remote.entity

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
object RoomEntity {
    data class Censored (
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

    data class Complete (
        @SerializedName("roomId")// @SerializedName("roomId")
        val roomId: String,

        @SerializedName("userId")
        val userid: String,

        @SerializedName("minute")
        val minute: Int,

        @SerializedName("title")
        val title: String,

        @SerializedName("description")
        val description: String,

        @SerializedName("expired")
        val expired: Boolean,

        @SerializedName("createdAt")
        val createdAt: Date,

        @SerializedName("updatedAt")
        val updatedAt: Date?,

        @SerializedName("questions")
        val questions: List<QuizEntity>,

        @SerializedName("participants")
        val participants: List<ParticipantEntity>
    )
}
