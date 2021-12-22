package com.dudegenuine.whoknows.ui.presenter

import com.dudegenuine.model.*

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/

data class ViewState(
    val loading: Boolean = false,
    val error: String = "", //Error? = null,

    val user: User? = null,
    val users: List<User> = emptyList(),

    val room: Room? = null,
    val rooms: List<Room> = emptyList(),

    val quiz: Quiz? = null,
    val questions: List<Quiz> = emptyList(),

    val result: Result? = null,
    val results: List<Result> = emptyList(),

    val participant: Participant? = null,
    val participants: List<Participant> = emptyList()){

    sealed class Error {
        object BadRequest: Error()
        object NetworkError: Error()
    }

    companion object {
        const val DONT_EMPTY = "Fields must not be blank."
    }
}