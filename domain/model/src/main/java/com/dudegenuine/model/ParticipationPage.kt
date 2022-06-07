package com.dudegenuine.model

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
class ParticipationPage(
    val quiz: Quiz.Complete,
    val questionIndex: Int,
    val totalQuestionsCount: Int,
    val showPrevious: Boolean,
    val showDone: Boolean) {

    var enableNext by mutableStateOf(false)
    var answer by mutableStateOf<Quiz.Answer.Exact?>(null)
    var isCorrect by mutableStateOf(false)
}