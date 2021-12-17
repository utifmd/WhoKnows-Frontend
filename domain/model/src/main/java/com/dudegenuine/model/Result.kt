package com.dudegenuine.model

import java.util.*

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
data class Result (
    val id: String,
    var roomId: String,
    var participantId: String,
    var userId: String,
    var correctQuiz: List<String>,
    var wrongQuiz: List<String>,
    var score: Int?,
    var createdAt: Date,
    var updatedAt: Date?
){
    val isPropsBlank: Boolean = roomId.isBlank() ||
            participantId.isBlank() ||
            userId.isBlank() ||
            correctQuiz.isEmpty() ||
            wrongQuiz.isEmpty() ||
            score != 0
}