package com.dudegenuine.model

import java.util.*

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
data class Quiz(
    val id: String,
    var roomId: String,
    var images: List<String>,
    var question: String,
    var options: List<String>,
    var answer: String,
    var createdBy: String,
    var createdAt: Date,
    var updatedAt: Date?
)
