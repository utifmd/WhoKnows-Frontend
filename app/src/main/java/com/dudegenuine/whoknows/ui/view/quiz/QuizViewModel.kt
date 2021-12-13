package com.dudegenuine.whoknows.ui.view.quiz

import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.Quiz
import com.dudegenuine.usecase.quiz.*
import com.dudegenuine.whoknows.ui.view.BaseViewModel
import com.dudegenuine.whoknows.ui.view.ViewState
import com.dudegenuine.whoknows.ui.view.ViewState.Companion.DONT_EMPTY
import com.dudegenuine.whoknows.ui.view.quiz.contract.IQuizViewModel
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
    private val getQuestionsUseCase: GetQuestions
): BaseViewModel(), IQuizViewModel {

    override fun postQuiz(quiz: Quiz) {
        if (quiz.roomId.isEmpty() ||
            quiz.question.isEmpty() ||
            quiz.options.isEmpty() ||
            quiz.answer.isEmpty() ||
            quiz.createdBy.isEmpty()){
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        quiz.apply { createdAt = Date() }

        postQuizUseCase(quiz)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getQuiz(id: String) {
        if (id.isEmpty()){
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        getQuizUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun patchQuiz(id: String, current: Quiz) {
        if (id.isEmpty() ||
            current.roomId.isEmpty() ||
            current.question.isEmpty() ||
            current.options.isEmpty() ||
            current.answer.isEmpty() ||
            current.createdBy.isEmpty()){
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        current.apply { updatedAt = Date() }

        patchQuizUseCase(id, current)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun deleteQuiz(id: String) {
        if (id.isEmpty()){
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        deleteQuizUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getQuestions(page: Int, size: Int) {
        if (size != 0){
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        getQuestionsUseCase(page, size)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }
}