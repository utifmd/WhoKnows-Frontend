package com.dudegenuine.whoknows.ui.vm.quiz.contract

import com.dudegenuine.model.Quiz
import com.dudegenuine.whoknows.ui.vm.file.IFilePresenter

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
interface IQuizViewModel: IFilePresenter {
    fun postQuiz(quiz: Quiz)
    fun getQuiz(id: String)
    fun patchQuiz(id: String, current: Quiz)
    fun deleteQuiz(id: String)
    fun getQuestions(page: Int, size: Int)
}