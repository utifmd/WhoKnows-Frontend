package com.dudegenuine.whoknows.ui.compose.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dudegenuine.model.Answer
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.Result
import com.dudegenuine.model.Room

/**
 * Mon, 20 Dec 2021
 * WhoKnows by utifmd
 **/
@Stable
class OnBoardingState(
    val quiz: Quiz,
    val questionIndex: Int,
    val totalQuestionsCount: Int,
    val showPrevious: Boolean,
    val showDone: Boolean) {
        var enableNext by mutableStateOf(false)
        var answer by mutableStateOf<Answer?>(null)
        var isCorrect by mutableStateOf(false)
}

sealed class RoomState {
    object CurrentRoom: RoomState() //{ var currentUserId by mutableStateOf("") }
    object CreateQuizzes: RoomState()

    data class BoardingQuiz(
        val room: Room,
        val list: List<OnBoardingState>): RoomState(){
            var currentQuestionIdx by mutableStateOf(0)
    }

    data class BoardingResult(
        val title: String,
        val data: Result?): RoomState()
}
