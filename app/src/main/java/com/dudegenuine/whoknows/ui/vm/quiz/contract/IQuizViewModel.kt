package com.dudegenuine.whoknows.ui.vm.quiz.contract

import androidx.paging.PagingData
import com.dudegenuine.model.Quiz
import com.dudegenuine.whoknows.ui.vm.file.IFileViewModel
import kotlinx.coroutines.flow.Flow

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
interface IQuizViewModel: IFileViewModel {
    companion object {
        const val BATCH_SIZE = 2
    }
    val questions: Flow<PagingData<Quiz.Complete>>

    fun postQuiz(quiz: Quiz.Complete)
    fun getQuiz(id: String)
    fun patchQuiz(id: String, current: Quiz.Complete)
    fun deleteQuiz(id: String)
    fun getQuestions(page: Int, size: Int)
}