package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.usecase.quiz.*

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
interface IQuizUseCaseModule {
    val postQuiz: PostQuiz
    val getQuiz: GetQuiz
    val patchQuiz: PatchQuiz
    val deleteQuiz: DeleteQuiz
    val getQuestions: GetQuestions
}