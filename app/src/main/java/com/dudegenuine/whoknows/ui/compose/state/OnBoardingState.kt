package com.dudegenuine.whoknows.ui.compose.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dudegenuine.model.PossibleAnswer
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.Result

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
        var possibleAnswer by mutableStateOf<PossibleAnswer?>(null)
}

sealed class RoomState {
    data class BoardingQuiz(
        val title: String,
        val listOnBoardingState: List<OnBoardingState>): RoomState(){
            var currentQuestionIdx by mutableStateOf(0)
        }
    data class BoardingResult(
        val title: String,
        val data: Result): RoomState()
}
