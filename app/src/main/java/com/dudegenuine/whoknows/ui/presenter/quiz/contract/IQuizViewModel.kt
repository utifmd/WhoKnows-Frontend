package com.dudegenuine.whoknows.ui.presenter.quiz.contract

import com.dudegenuine.model.Quiz

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
interface IQuizViewModel {
    fun postQuiz(quiz: Quiz)
    fun getQuiz(id: String)
    fun patchQuiz(id: String, current: Quiz)
    fun deleteQuiz(id: String)
    fun getQuestions(page: Int, size: Int)
}