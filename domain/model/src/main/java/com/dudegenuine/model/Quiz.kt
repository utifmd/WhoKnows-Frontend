package com.dudegenuine.model

import com.dudegenuine.model.common.ImageUtil.strOf
import java.util.*

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
object Quiz {
    data class Complete(
        val id: String,
        var roomId: String,
        var images: List<String>,
        var question: String,
        var options: List<String>,
        var answer: Answer.Possible?,
        var createdBy: String,
        var createdAt: Date,
        var updatedAt: Date?,
        val user: User.Censored?) {

        val isPropsBlank: Boolean = roomId.isBlank() ||
            question.isBlank() || options.isEmpty() ||
            createdBy.isBlank() // answer.isBlank() ||
    }

    object Answer {
        sealed class Possible(val type: String) {
            data class SingleChoice(val answer: String): Possible(strOf<SingleChoice>())
            data class MultipleChoice(val answers: Set<String>): Possible(strOf<MultipleChoice>())
            data class Action(val answer: Quiz.Action.Result): Possible(strOf<Action>())
            data class Slider(val answer: Float): Possible(strOf<Slider>())
        }

        data class Exact(
            val type: String,
            val answer: String? = null,
            val answers: Set<String>? = null
        )
    }

    object Option {
        sealed class Possible {
            data class SingleChoice(val options: List<String>): Possible()
            data class MultipleChoice(val options: List<String>): Possible()
            data class Action(val label: Int, val actionType: Quiz.Action.Type): Possible()
            data class Slider(
                val range: ClosedFloatingPointRange<Float>,
                val steps: String,
                val startText: String,
                val endText: String,
                val defaultValue: Float = range.start): Possible()
        }
    }

    object Action {
        enum class Type { PICK_DATE, TAKE_PHOTO, SELECT_CONTACT }

        sealed class Result {
            data class Date(val date: String): Result()
            data class Photo(val uri: String): Result()
            data class Contact(val contact: String): Result()
        }
    }
}