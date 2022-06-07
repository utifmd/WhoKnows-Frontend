package com.dudegenuine.model

import java.util.*

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
data class Participant (
    val id: String,
    var roomId: String,
    var userId: String,
    var currentPage: String,
    var timeLeft: Int?,
    var expired: Boolean,
    var isCurrentUser: Boolean,
    var createdAt: Date,
    var updatedAt: Date?,
    var user: User.Censored?){ //var results: List<Result>
    val isPropsBlank: Boolean =
        roomId.isBlank() || userId.isBlank() ||
                currentPage.isBlank()
}
