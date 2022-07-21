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

        @SerializedName("token")
        val token: String,

        @SerializedName("expired")
        val expired: Boolean,

        @SerializedName("private")
        var private: Boolean?,

        @SerializedName("user")
        val user: UserEntity.Censored?,

        @SerializedName("impressions")
        val impressions: List<ImpressionEntity>,

        @SerializedName("questionSize")
        val questionSize: Int,

        @SerializedName("participantSize")
        val participantSize: Int,

        @SerializedName("participantIds")
        val participantIds: List<String>,
    )

    data class Complete (
        @SerializedName("roomId")// @SerializedName("roomId")usernameOwner
        val roomId: String,

        @SerializedName("userId")
        val userId: String,

        @SerializedName("minute")
        val minute: Int,

        @SerializedName("title")
        val title: String,

        @SerializedName("description")
        val description: String,

        @SerializedName("token")
        val token: String,

        @SerializedName("expired")
        val expired: Boolean,

        @SerializedName("private")
        val private: Boolean?,

        @SerializedName("createdAt")
        val createdAt: Date,

        @SerializedName("updatedAt")
        val updatedAt: Date?,

        @SerializedName("impressions")
        val impressions: List<ImpressionEntity>,

        @SerializedName("user")
        val user: UserEntity.Censored?,

        @SerializedName("questions")
        val questions: List<QuizEntity>,

        @SerializedName("participants")
        val participants: List<ParticipantEntity>
    )
}
