package com.dudegenuine.whoknows.ui.compose.state

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.text.input.TextFieldValue
import com.dudegenuine.model.Answer
import com.dudegenuine.model.PossibleAnswer
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.common.ImageUtil
import java.util.*

/**
 * Mon, 10 Jan 2022
 * WhoKnows by utifmd
 **/
sealed class QuizState {
    class FormState: QuizState() {
        private val _roomId = mutableStateOf("")
        val roomId: String
            get() = _roomId.value

        private val _currentQuestion = mutableStateOf(TextFieldValue(""))
        val currentQuestion: TextFieldValue
            get() = _currentQuestion.value

        private val _currentOption = mutableStateOf(TextFieldValue(""))
        val currentOption: TextFieldValue
            get() = _currentOption.value

        private val _currentAnswer = mutableStateOf<Answer?>(null)
        val currentAnswer: Answer?
            get() = _currentAnswer.value

        private val _options = mutableStateListOf<String>()
        val options: List<String>
            get() = _options

        private val _images = mutableStateListOf<ByteArray>()
        val images: List<ByteArray>
            get() = _images

        private val _selectedAnswer = mutableStateOf<PossibleAnswer?>(null)
        private val selectedAnswer: PossibleAnswer?
            get() = _selectedAnswer.value

        private val setOfAnswers = mutableSetOf<String>()

        val isValid: MutableState<Boolean>
            get() = mutableStateOf(
                selectedAnswer != null &&
                        currentQuestion.text.isNotBlank() &&
                        images.isNotEmpty() &&
                        options.isNotEmpty()
            )

        fun onResultImage (context: Context, result: Uri?) {
            result?.let { uri ->
                val scaledImage = ImageUtil.adjustImage(context, uri)

                if(!images.contains(scaledImage)) _images.add(scaledImage)
            }
        }

        fun onOptionKeyEvent (it: KeyEvent): Boolean {
            if (it.nativeKeyEvent.keyCode == android.view.KeyEvent.KEYCODE_ENTER)
                onPushedOption()
            return false
        }

        fun onPushedOption () {
            if (currentOption.text.isNotBlank()){
                _options.add(currentOption.text.trim()).also {
                    _currentOption.value = TextFieldValue("")
                }
            }
        }

        fun onQuestionValueChange (it: String) {
            _currentQuestion.value = TextFieldValue(it)
        }

        fun onOptionValueChange (it: String) {
            _currentOption.value = TextFieldValue(it)
        }

        fun onUserIdValueChange (it: String){
            _roomId.value = it
        }

        fun onSelectedAnswerValue (it: PossibleAnswer?) {
            _selectedAnswer.value = it
        }

        fun onAnsweredSingle (newAnswer: String) {
            onSelectedAnswerValue(PossibleAnswer.SingleChoice(newAnswer))
        }

        fun onAnsweredMultiple (newAnswer: String, selected :Boolean) {
            if (selected) setOfAnswers.add(newAnswer)
            else setOfAnswers.remove(newAnswer)

            onSelectedAnswerValue(PossibleAnswer.MultipleChoice(setOfAnswers))
        }

        fun onImagesRemoveAt (it: Int) {
            _images.removeAt(it)
        }

        val postModel: State<Quiz>
            get() = mutableStateOf(
                Quiz(
                    "QIZ-${UUID.randomUUID()}",
                    roomId,// "ROM-f80365e5-0e65-4674-9e7b-bee666b62bda",
                    images = emptyList(),
                    question = currentQuestion.text.trim(),
                    options = options,
                    answer = selectedAnswer,
                    createdBy = "Diyanti Ratna Puspita Sari",
                    createdAt = Date(),
                    updatedAt = null
                )
            )
    }
}