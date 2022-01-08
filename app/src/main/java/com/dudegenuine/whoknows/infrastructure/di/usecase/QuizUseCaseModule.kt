package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.contract.IQuizRepository
import com.dudegenuine.usecase.quiz.*
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IQuizUseCaseModule

/**
 * Sat, 08 Jan 2022
 * WhoKnows by utifmd
 **/
class QuizUseCaseModule(
    private val repository: IQuizRepository,

    override val postQuiz: PostQuiz =
        PostQuiz(repository),

    override val getQuiz: GetQuiz =
        GetQuiz(repository),

    override val patchQuiz: PatchQuiz =
        PatchQuiz(repository),

    override val deleteQuiz: DeleteQuiz =
        DeleteQuiz(repository),

    override val getQuestions: GetQuestions =
        GetQuestions(repository)

): IQuizUseCaseModule