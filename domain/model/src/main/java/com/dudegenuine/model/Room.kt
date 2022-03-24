package com.dudegenuine.model

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import java.util.*

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
object Room {
    data class Censored(
        val roomId: String,
        val userId: String,
        val minute: Int,
        val title: String,
        val description: String,
        val expired: Boolean,
    )
    
    data class Complete(
        val id: String,
        val userId: String,
        var minute: Int,
        var title: String,
        var description: String,
        var expired: Boolean,
        var createdAt: Date,
        var updatedAt: Date?,
        var questions: List<Quiz.Complete>,
        var participants: List<Participant>) {
        val isPropsBlank: Boolean =
            minute == 0 || title.isBlank() || userId.isBlank() || description.isBlank()
    
    }
    
    sealed class State {
        object CurrentRoom: State() //{ var currentUserId by mutableStateOf("") }

        data class BoardingQuiz(
            val participantId: String,
            val participant: User.Censored,
            val roomId: String,
            val userId: String,
            val roomTitle: String,
            val roomDesc: String,
            val roomMinute: Int,
            val quizzes: List<OnBoardingState>): State() {
            var currentQuestionIdx by mutableStateOf(0) //var duration: Double = (room.minute.toFloat() * 60).toDouble()
        }

        data class BoardingResult(
            val title: String,
            val data: Result?): State()

        class FormState: State() {
            private val _roomId = mutableStateOf("")
            val roomId: String
                get() = _roomId.value

            private val _title = mutableStateOf(TextFieldValue(text = ""))
            val title: TextFieldValue
                get() = _title.value

            private val _desc = mutableStateOf(TextFieldValue(text = ""))
            val desc: TextFieldValue
                get() = _desc.value

            private val _timer = mutableStateOf(0.0)
            val timer: Double
                get() = _timer.value

            private val _minute = mutableStateOf(TextFieldValue(text = ""))
            val minute: TextFieldValue
                get() = _minute.value

            val isPostValid: Boolean
                get() = mutableStateOf(
                    title.text.isNotBlank() &&
                            desc.text.isNotBlank() &&
                            minute.text.isNotBlank()).value

            val room: Complete get() = mutableStateOf(Complete(
                id = "ROM-${UUID.randomUUID()}", userId = "",
                minute = if(minute.text.isBlank()) 0 else minute.text.toInt(), title = title.text, description = desc.text,
                expired = false, questions = emptyList(), participants = emptyList(), createdAt = Date(), updatedAt = null)).value

            val notification: Notification get() = mutableStateOf(Notification(
                notificationId = "NTF-${UUID.randomUUID()}",
                userId = "", roomId = "", event = "", seen = false, recipientId = "", createdAt = Date(), null, null)).value

            val result: Result = mutableStateOf(Result(
                resultId = "RSL-${UUID.randomUUID()}",
                roomId = "", /*participantId = "",*/ userId = "", correctQuiz = emptyList(), wrongQuiz = emptyList(), score = 0,
                createdAt = Date(),
                updatedAt = null, user = null)).value

            private val _participantModel = mutableStateOf(
                Participant(
                    id = "PPN-${UUID.randomUUID()}",
                    roomId = "", userId = "", currentPage = "0", timeLeft = null, expired = false,
                    createdAt = Date(), updatedAt = null, user = null)
            )

            val participant: Participant
                get() = _participantModel.value

            val isGetValid: Boolean
                get() = mutableStateOf(roomId.isNotBlank()).value

            fun onTitleChange (it: String){
                _title.value = TextFieldValue(text = it)
            }

            fun onDescChange (it: String){
                _desc.value = TextFieldValue(text = it)
            }

            fun onMinuteChange (it: String){
                _minute.value = TextFieldValue(text = it)
            }

            fun onRoomIdChange(id: String) {
                _roomId.value = id
            }

            fun onTimerChange(time: Double){
                _timer.value = time
            }
        }

        @Stable
        class OnBoardingState(
            val quiz: Quiz.Complete,
            val questionIndex: Int,
            val totalQuestionsCount: Int,
            val showPrevious: Boolean,
            val showDone: Boolean) {
            var enableNext by mutableStateOf(false)
            var answer by mutableStateOf<Quiz.Answer.Exact?>(null)
            var isCorrect by mutableStateOf(false)
        }
    }
}
