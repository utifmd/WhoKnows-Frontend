package com.dudegenuine.whoknows.ui.compose.state

import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import com.dudegenuine.model.Answer
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.Result
import com.dudegenuine.model.Room
import java.util.*

/**
 * Mon, 20 Dec 2021
 * WhoKnows by utifmd
 **/
sealed class RoomState {
    object CurrentRoom: RoomState() //{ var currentUserId by mutableStateOf("") }

    data class BoardingQuiz(
        val room: Room,
        val list: List<OnBoardingState>): RoomState(){
            var currentQuestionIdx by mutableStateOf(0)
    }

    data class BoardingResult(
        val title: String,
        val data: Result?): RoomState()

    class CreateRoom: RoomState(){
        private val _title = mutableStateOf(TextFieldValue(text = ""))
        val title: TextFieldValue
            get() = _title.value

        private val _desc = mutableStateOf(TextFieldValue(text = ""))
        val desc: TextFieldValue
            get() = _desc.value

        private val _minute = mutableStateOf(TextFieldValue(text = ""))
        val minute: TextFieldValue
            get() = _minute.value

        val isValid: MutableState<Boolean>
            get() = mutableStateOf(
                title.text.isNotBlank() && desc.text.isNotBlank() && minute.text.isNotBlank())

        val model: MutableState<Room>
            get() = mutableStateOf(Room(
                id = "ROM-${UUID.randomUUID()}",
                userId = "USR-0001",
                minute = minute.text.toInt(),
                title = title.text,
                description = desc.text,
                expired = false,
                createdAt = Date(),
                updatedAt = null,
                questions = emptyList(),
                participants = emptyList(),
            ))

        fun onTitleChange (it: String){
            _title.value = TextFieldValue(text = it)
        }

        fun onDescChange (it: String){
            _desc.value = TextFieldValue(text = it)
        }

        fun onMinuteChange (it: String){
            _minute.value = TextFieldValue(text = it)
        }
    }
}

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