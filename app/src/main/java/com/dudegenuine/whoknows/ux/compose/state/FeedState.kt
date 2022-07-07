package com.dudegenuine.whoknows.ux.compose.state

import androidx.paging.PagingData
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.Room
import com.dudegenuine.model.User

/**
 * Sat, 25 Jun 2022
 * WhoKnows by utifmd
 **/
data class FeedState(
    val pagingRooms: PagingData<Room.Censored>? = null,
    val pagingQuiz: PagingData<Quiz.Complete>? = null,
    val pagingParticipant: PagingData<User.Censored>? = null,
)
