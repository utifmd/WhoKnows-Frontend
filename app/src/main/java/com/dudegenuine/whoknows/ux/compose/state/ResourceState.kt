package com.dudegenuine.whoknows.ux.compose.state

import androidx.paging.PagingData
import com.dudegenuine.model.*
import kotlinx.coroutines.flow.Flow

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/

data class ResourceState(
    val loading: Boolean = false,
    val message: String = "", //Error? = null,
    val error: String = "", //Error? = null,
    val workerOutput: String = "",

    val feedRoomPageDataFlow: Flow<PagingData<Room.Censored>>? = null,
    val feedQuizPageDataFlow: Flow<PagingData<Quiz.Complete>>? = null,

    val user: User.Complete? = null,
    val userCensored: User.Censored? = null,
    val users: List<User.Complete>? = null,
    val usersCensored: List<User.Censored>? = null,

    val room: Room.Complete? = null,
    val roomCensored: Room.Censored? = null,
    val rooms: List<Room.Complete>? = null,
    val roomsCensored: List<Room.Censored>? = null,

    val quiz: Quiz.Complete? = null,
    val questions: List<Quiz.Complete>? = null,

    val result: Result? = null,
    val results: List<Result>? = null,

    val participant: Participant? = null,
    val participants: List<Participant>? = null,

    //val participation: Participation? = null,

    val notification: Notification? = null,
    val notifications: List<Notification>? = null,

    val file: File? = null,
    val files: List<File>? = null,){

    data class Auth(
        val invalidated: Boolean = false,
        val loading: Boolean = false,
        val error: String = "",
        val message: String = "",
        val user: User.Complete? = null
    )
    data class Boarding(
        val loading: Boolean = false,
        val error: String = "",
        val data: Participation? = null
    )

    companion object {
        const val DONT_EMPTY = "Text must not be blank."
        const val USERNAME_EXIST = "Username already used."
        const val MISS_MATCH = "Password mismatch."
        const val DONT_EMPTY_IMG = "Pick single picture first."
        const val CHECK_CONN = "Check your internet connection and try again."
        const val NULL_STATE = "State is null."
        const val NO_QUESTION = "There is no question for this room."

        const val DESC_TOO_LONG = "Description must not more then 225 chars."
        const val PUSH_NOT_SENT = "Push notification not sent."
    }
}