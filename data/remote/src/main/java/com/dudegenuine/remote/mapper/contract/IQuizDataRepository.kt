package com.dudegenuine.remote.mapper.contract

import com.dudegenuine.model.Quiz
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.QuizEntity

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IQuizDataRepository {
    fun asEntity(user: Quiz): QuizEntity
    fun asQuiz(entity: QuizEntity): Quiz
    fun asQuiz(response: Response<QuizEntity>): Quiz
    fun asQuestions(response: Response<List<QuizEntity>>): List<Quiz>
}