package com.dudegenuine.whoknows.ui.compose.state

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.input.key.KeyEvent
import com.dudegenuine.model.Answer
import com.dudegenuine.model.PossibleAnswer
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.common.ImageUtil
import java.util.*

/**
 * Mon, 10 Jan 2022
 * WhoKnows by utifmd
 **/
data class QuizState(
    val currentQuestion: MutableState<String> = mutableStateOf(""),
    val currentOption: MutableState<String> = mutableStateOf(""),
    val currentAnswer: MutableState<Answer?> = mutableStateOf(null),
    val options: SnapshotStateList<String> = mutableStateListOf(),
    val images: SnapshotStateList<ByteArray> = mutableStateListOf()) {

    private val _selectedAnswer = mutableStateOf<PossibleAnswer?>(null)
    private val selectedAnswer: State<PossibleAnswer?>
        get() = _selectedAnswer

    private val setOfAnswers = mutableSetOf<String>()

    val isValid: MutableState<Boolean>
        get() = mutableStateOf(
            selectedAnswer.value != null &&
                    currentQuestion.value.isNotBlank() &&
                    images.isNotEmpty() &&
                    options.isNotEmpty()
        )

    val onResultImage: (Context, Uri?) -> Unit = { context, result ->
        result?.let { uri ->
            val scaledImage = ImageUtil.adjustImage(context, uri)

            if(!images.contains(scaledImage)) images.add(scaledImage)
        }
    }

    val onOptionKeyEvent: (KeyEvent) -> Boolean = {
        if (it.nativeKeyEvent.keyCode == android.view.KeyEvent.KEYCODE_ENTER)
            onPushedOption()
        false
    }

    val onPushedOption: () -> Unit = {
        if (currentOption.value.isNotBlank()){
            options.add(currentOption.value.trim()).also {
                currentOption.value = ""
            }
        }
    }

    val onQuestionValueChange: (String) -> Unit = {
        currentQuestion.value = it
    }

    val onOptionValueChange: (String) -> Unit = {
        currentOption.value = it
    }

    val onSelectedAnswerValue: (PossibleAnswer?) -> Unit = {
        _selectedAnswer.value = it
    }

    val onAnsweredSingle: (String) -> Unit = { newAnswer ->
        onSelectedAnswerValue(PossibleAnswer.SingleChoice(newAnswer))
    }

    val onAnsweredMultiple: (String, Boolean) -> Unit = { newAnswer, selected ->
        if (selected) setOfAnswers.add(newAnswer)
        else setOfAnswers.remove(newAnswer)

        onSelectedAnswerValue(PossibleAnswer.MultipleChoice(setOfAnswers))
    }

    val model: MutableState<Quiz>
        get() = mutableStateOf(Quiz(
        "QIZ-${UUID.randomUUID()}",
        "B00001",// "ROM-f80365e5-0e65-4674-9e7b-bee666b62bda",
            images = emptyList(),
            question = currentQuestion.value.trim(),
            options = options.toList(),
            answer = selectedAnswer.value,
            createdBy = "Diyanti Ratna Puspita Sari",
            createdAt = Date(),
            updatedAt = null
        )
    )
}