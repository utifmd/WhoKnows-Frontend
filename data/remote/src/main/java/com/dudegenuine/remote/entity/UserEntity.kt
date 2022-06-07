package com.dudegenuine.remote.entity

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
object UserEntity{
    data class Censored(
        @SerializedName("userId")
        val userId: String,

        @SerializedName("fullName")
        val fullName: String,

        @SerializedName("username")
        val username: String,

        @SerializedName("profileUrl")
        val profileUrl: String,

        @SerializedName("tokens")
        var tokens: List<String>
    )

    data class Complete (
        @SerializedName("userId")// @SerializedName("userId")
        val userId: String,

        @SerializedName("fullName")
        val fullName: String,

        @SerializedName("email")
        val email: String,

        @SerializedName("phone")
        val phone: String,

        @SerializedName("username")
        val username: String,

        @SerializedName("password")
        val password: String,

        @SerializedName("profileUrl")
        val profileUrl: String,

        @SerializedName("createdAt")
        val createdAt: Date,

        @SerializedName("updatedAt")
        val updatedAt: Date?,

        @SerializedName("tokens")
        var tokens: List<String>,

        @SerializedName("participants")
        val participants: List<ParticipantEntity>,

        @SerializedName("rooms")
        val rooms: List<RoomEntity.Censored>,

        @SerializedName("notifications")
        val notifications: List<NotificationEntity>)
}
