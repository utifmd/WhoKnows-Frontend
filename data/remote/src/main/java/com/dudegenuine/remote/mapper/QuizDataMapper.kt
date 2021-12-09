package com.dudegenuine.remote.mapper

import com.dudegenuine.model.Quiz
import com.dudegenuine.remote.entity.QuizEntity
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.mapper.contract.IQuizDataMapper

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
class QuizDataMapper: IQuizDataMapper {
    override fun asEntity(quiz: Quiz): QuizEntity {
        return QuizEntity(quiz.id, quiz.roomId, quiz.images, quiz.question, quiz.options, quiz.answer, quiz.createdBy, quiz.createdAt, quiz.updatedAt)
    }

    override fun asQuiz(entity: QuizEntity): Quiz {
        return Quiz(entity.id, entity.roomId, entity.images, entity.question, entity.options, entity.answer, entity.createdBy, entity.createdAt, entity.updatedAt)
    }

    override fun asQuiz(response: Response<QuizEntity>): Quiz {
        return when(response.data){
            is QuizEntity -> asQuiz(response.data)
            else -> throw IllegalStateException()
        }
    }

    override fun asQuestions(response: Response<List<QuizEntity>>): List<Quiz> {
        return when(response.data){
            is List<*> -> {
                val questions = response.data.filterIsInstance<QuizEntity>()

                questions.map { asQuiz(it) }
            }
            else -> throw IllegalStateException()
        }
    }
}