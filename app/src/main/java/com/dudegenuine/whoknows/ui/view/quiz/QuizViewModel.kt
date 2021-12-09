package com.dudegenuine.whoknows.ui.view.quiz

import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.Quiz
import com.dudegenuine.usecase.quiz.*
import com.dudegenuine.whoknows.ui.view.BaseViewModel
import com.dudegenuine.whoknows.ui.view.quiz.contract.IQuizViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        postQuizUseCase(quiz)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getQuiz(id: String) {
        getQuizUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun patchQuiz(id: String, current: Quiz) {
        patchQuizUseCase(id, current)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun deleteQuiz(id: String) {
        deleteQuizUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getQuestions(page: Int, size: Int) {
        getQuestionsUseCase(page, size)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }
}