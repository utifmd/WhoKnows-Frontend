package com.dudegenuine.remote.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
data class QuizEntity(

    @SerializedName("quizId")
    val id: String,

    @SerializedName("roomId")
    val roomId: String,

    @SerializedName("images")
    val images: List<String>,

    @SerializedName("question")
    val question: String,

    @SerializedName("options")
    val options: List<String>,

    @SerializedName("answer")
    val answer: String,

    @SerializedName("createdBy")
    val createdBy: String,

    @SerializedName("createdAt")
    val createdAt: Date,

    @SerializedName("updatedAt")
    val updatedAt: Date?

): Serializable
