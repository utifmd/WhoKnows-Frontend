package com.dudegenuine.whoknows.ui.compose.state

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    class FormState: RoomState() {
        private val _roomId = mutableStateOf("")
        val roomId: String
            get() = _roomId.value

        private val _title = mutableStateOf(TextFieldValue(text = ""))
        val title: TextFieldValue
            get() = _title.value

        private val _desc = mutableStateOf(TextFieldValue(text = ""))
        val desc: TextFieldValue
            get() = _desc.value

        private val _minute = mutableStateOf(TextFieldValue(text = ""))
        val minute: TextFieldValue
            get() = _minute.value

        val isPostValid: Boolean
            get() = mutableStateOf(
                title.text.isNotBlank() &&
                desc.text.isNotBlank() && minute.text.isNotBlank()
            ).value

        private val _model = mutableStateOf(
            Room(
                id = "ROM-${UUID.randomUUID()}",
                userId = "",
                minute = if(minute.text.isBlank()) 0 else minute.text.toInt(),
                title = title.text,
                description = desc.text,
                expired = false,
                questions = emptyList(),
                participants = emptyList(),
                createdAt = Date(),
                updatedAt = null ))
        val model: Room get() = _model.value

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

        fun onModelValueChange(roomId: String) {
            val result = model.copy(id = roomId)

            Log.d("onModelValueChange: ", roomId)
            Log.d("onModelValueChange: ", result.toString())

            /*_model.value = model.copy()*/
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