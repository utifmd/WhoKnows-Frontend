package com.dudegenuine.whoknows.ui.presenter

import com.dudegenuine.model.*

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/

data class ResourceState(
    val loading: Boolean = false,
    val error: String = "", //Error? = null,

    val user: User? = null,
    val users: List<User>? = null,

    val room: Room? = null,
    val rooms: List<Room>? = null,

    val quiz: Quiz? = null,
    val questions: List<Quiz>? = null,

    val result: Result? = null,
    val results: List<Result>? = null,

    val participant: Participant? = null,
    val participants: List<Participant>? = null,

    val file: File? = null,
    val files: List<File>? = null,){

    /*val userJson: String? get() = user?.let { Gson().toJson(user) }*/

    /*sealed class Error {
        object Popping: Error()
        object Static: Error()
    }*/

    companion object {
        const val DONT_EMPTY = "Fields must not be blank."
        const val NULL_STATE = "State is null."
    }
}