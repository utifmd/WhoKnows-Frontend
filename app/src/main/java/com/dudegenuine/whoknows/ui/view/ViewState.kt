package com.dudegenuine.whoknows.ui.view

import com.dudegenuine.model.*

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/

data class ViewState(
    val loading: Boolean = false,
    val error: String = "",

    val user: User? = null,
    val users: List<User>? = null,

    val room: Room? = null,
    val rooms: List<Room>? = null,

    val quiz: Quiz? = null,
    val questions: List<Quiz>? = null,

    val result: Result? = null,
    val results: List<Result>? = null,

    val participant: Participant? = null,
    val participants: List<Participant>? = null
)