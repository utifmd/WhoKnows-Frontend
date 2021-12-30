package com.dudegenuine.whoknows.ui.presenter.quiz

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.input.key.KeyEvent
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.Answer
import com.dudegenuine.model.PossibleAnswer
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.common.Utility
import com.dudegenuine.model.common.Utility.strOf
import com.dudegenuine.usecase.quiz.*
import com.dudegenuine.whoknows.ui.presenter.BaseViewModel
import com.dudegenuine.whoknows.ui.presenter.ResourceState
import com.dudegenuine.whoknows.ui.presenter.ResourceState.Companion.DONT_EMPTY
import com.dudegenuine.whoknows.ui.presenter.quiz.contract.IQuizViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
@HiltViewModel
class QuizViewModel
    @Inject constructor(
    private val postQuizUseCase: PostQuiz,
    private val getQuizUseCase: GetQuiz,
    private val patchQuizUseCase: PatchQuiz,
    private val deleteQuizUseCase: DeleteQuiz,
    private val getQuestionsUseCase: GetQuestions): BaseViewModel(), IQuizViewModel {
    private val TAG: String = strOf<QuizViewModel>()

    val resourceState: State<ResourceState> = _state

    val question = mutableStateOf("")
    val images = mutableStateListOf<Bitmap>()

    val options = mutableStateListOf<String>()
    val mOption = mutableStateOf("")

    private val selectedAnswer = mutableStateOf<PossibleAnswer?>(null)
    private val multipleAnswer = mutableSetOf<String>()
    val mAnswer = mutableStateOf<Answer?>(null)

    // init { getQuestions(0, 10) }

    val onResultImage: (Context, Uri?) -> Unit = { context, result ->
        result?.let { uri ->
            val item = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(item)
            if(!images.contains(bitmap)) images.add(bitmap)
            item?.close()
        }
    }

    val onPushedOption: () -> Unit = {
        options.add(mOption.value).apply {
            mOption.value = ""
        }
    }

    val onOptionKeyEvent: (KeyEvent) -> Boolean = {
        if (it.nativeKeyEvent.keyCode == android.view.KeyEvent.KEYCODE_ENTER && mOption.value.isNotBlank())
            onPushedOption()
        false
    }

    val onAnsweredSingle: (String) -> Unit = { newAnswer ->
        selectedAnswer.value = PossibleAnswer.SingleChoice(newAnswer)
    }

    val onAnsweredMultiple: (String, Boolean) -> Unit = { newAnswer, selected ->
        if (selected) multipleAnswer.add(newAnswer)
        else multipleAnswer.remove(newAnswer)

        selectedAnswer.value = PossibleAnswer.MultipleChoice(multipleAnswer)
    }

    val onPostPressed: () -> Unit = {
        val model = Quiz(
            "QIZ-${UUID.randomUUID()}",
            "ROM-f80365e5-0e65-4674-9e7b-bee666b62bda",
            images = images.map { Utility.asBase64(it) },
            question = question.value,
            options = options.toList(),
            answer = selectedAnswer.value,
            createdBy = "Diyanti Ratna Puspita Sari",
            createdAt = Date(),
            updatedAt = null
        )

        Log.d(TAG, model.toString())

        // postQuiz(model)
    }

    override fun postQuiz(quiz: Quiz) {
        if (quiz.roomId.isBlank() || quiz.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        postQuizUseCase(quiz)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getQuiz(id: String) {
        if (id.isBlank()){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        getQuizUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun patchQuiz(id: String, current: Quiz) {
        if (id.isBlank() || current.isPropsBlank){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        current.apply { updatedAt = Date() }

        patchQuizUseCase(id, current)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun deleteQuiz(id: String) {
        if (id.isBlank()){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        deleteQuizUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getQuestions(page: Int, size: Int) {
        if (size == 0){
            _state.value = ResourceState(error = DONT_EMPTY)
            return
        }

        getQuestionsUseCase(page, size)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }
}