package com.dudegenuine.model

import com.dudegenuine.model.common.Utility.strOf
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
    var answer: PossibleAnswer?,
    var createdBy: String,
    var createdAt: Date,
    var updatedAt: Date?) {
    val isPropsBlank: Boolean = roomId.isBlank() ||
            question.isBlank() || options.isEmpty() ||
            createdBy.isBlank() // answer.isBlank() ||
}

enum class QuizActionType { PICK_DATE, TAKE_PHOTO, SELECT_CONTACT }

sealed class QuizActionResult {
    data class Date(val date: String): QuizActionResult()
    data class Photo(val uri: String): QuizActionResult()
    data class Contact(val contact: String): QuizActionResult()
}

sealed class PossibleAnswer(val type: String) {
    data class SingleChoice(val answer: String): PossibleAnswer(strOf<SingleChoice>())
    data class MultipleChoice(val answers: Set<String>): PossibleAnswer(strOf<MultipleChoice>())
    data class Action(val answer: QuizActionResult): PossibleAnswer(strOf<Action>())
    data class Slider(val answer: Float): PossibleAnswer(strOf<Slider>())
}

data class Answer(
    val type: String,
    val answer: String? = null,
    val answers: Set<String>? = null
)

sealed class PossibleOption {
    data class SingleChoice(val options: List<String>): PossibleOption()
    data class MultipleChoice(val options: List<String>): PossibleOption()
    data class Action(val label: Int, val actionType: QuizActionType): PossibleOption()
    data class Slider(
        val range: ClosedFloatingPointRange<Float>,
        val steps: String,
        val startText: String,
        val endText: String,
        val defaultValue: Float = range.start
    ): PossibleOption()
}