package com.dudegenuine.model

import java.util.*

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
data class Room(
    val id: String,
    val userId: String,
    var minute: Int,
    var title: String,
    var description: String,
    var expired: Boolean,
    var createdAt: Date,
    var updatedAt: Date?,
    var questions: List<Quiz>,
    var participants: List<Participant>
){
    val isPropsBlank: Boolean =
        minute == 0 || title.isBlank() || userId.isBlank() || description.isBlank()
}