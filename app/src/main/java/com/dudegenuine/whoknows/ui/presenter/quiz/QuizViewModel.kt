package com.dudegenuine.whoknows.ui.presenter.quiz

import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.common.Utility
import com.dudegenuine.usecase.quiz.*
import com.dudegenuine.whoknows.ui.presenter.BaseViewModel
import com.dudegenuine.whoknows.ui.presenter.ViewState
import com.dudegenuine.whoknows.ui.presenter.ViewState.Companion.DONT_EMPTY
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
    private val TAG: String = Utility.strOf<QuizViewModel>()

    val state: State<ViewState> = _state

//    init {
//        getQuestions(0, 10)
//    }

    override fun postQuiz(quiz: Quiz) {
        if (quiz.roomId.isBlank() || quiz.isPropsBlank){
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        postQuizUseCase(quiz)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getQuiz(id: String) {
        if (id.isBlank()){
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        getQuizUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun patchQuiz(id: String, current: Quiz) {
        if (id.isBlank() || current.isPropsBlank){
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        current.apply { updatedAt = Date() }

        patchQuizUseCase(id, current)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun deleteQuiz(id: String) {
        if (id.isBlank()){
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        deleteQuizUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getQuestions(page: Int, size: Int) {
        if (size == 0){
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        getQuestionsUseCase(page, size)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }
}