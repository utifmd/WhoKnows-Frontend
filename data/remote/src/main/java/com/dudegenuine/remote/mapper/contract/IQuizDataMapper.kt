package com.dudegenuine.remote.mapper.contract

import androidx.paging.PagingSource
import com.dudegenuine.model.Quiz
import com.dudegenuine.remote.entity.QuizEntity
import com.dudegenuine.remote.entity.Response

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IQuizDataMapper {
    fun asEntity(quiz: Quiz.Complete): QuizEntity
    fun asQuiz(entity: QuizEntity): Quiz.Complete
    fun asQuiz(response: Response<QuizEntity>): Quiz.Complete
    fun asQuestions(response: Response<List<QuizEntity>>): List<Quiz.Complete>

    fun asPagingSource(onEvent: suspend (Int) -> List<Quiz.Complete>): PagingSource<Int, Quiz.Complete>
}