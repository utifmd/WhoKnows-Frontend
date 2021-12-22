package com.dudegenuine.remote.mapper

import com.dudegenuine.model.Answer
import com.dudegenuine.model.PossibleAnswer
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.common.Utility.strOf
import com.dudegenuine.remote.entity.QuizEntity
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.mapper.contract.IQuizDataMapper
import com.google.gson.Gson
import javax.inject.Inject

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
class QuizDataMapper
    @Inject constructor(
    val gson: Gson): IQuizDataMapper {
    val TAG: String = strOf<QuizDataMapper>()

    override fun asEntity(quiz: Quiz): QuizEntity {
        val strAnswer: String = gson.toJson(quiz.answer)

        // Log.d(TAG, "asEntity: $result")
        return QuizEntity(
            quiz.id, quiz.roomId, quiz.images, quiz.question,
            quiz.options, strAnswer, quiz.createdBy, quiz.createdAt, quiz.updatedAt
        )
    }

    override fun asQuiz(entity: QuizEntity): Quiz {
        val possibility: Answer = gson.fromJson(entity.answer, Answer::class.java)
        val result = Quiz(entity.id, entity.roomId, entity.images, entity.question, entity.options, null, entity.createdBy, entity.createdAt, entity.updatedAt)

        return when(possibility.type){
            strOf<PossibleAnswer.SingleChoice>() -> result.apply {
                answer = PossibleAnswer.SingleChoice(possibility.answer)
            }
            strOf<PossibleAnswer.MultipleChoice>() -> {
                val setData: Set<*> = gson.fromJson(possibility.answer, Set::class.java)
                val data: List<String> = setData.map { it as String }
                result.apply {
                    answer = PossibleAnswer.MultipleChoice(data.toSet())
                }
            }
//            strOf<PossibleAnswer.Slider>() -> {}
//            strOf<PossibleAnswer.Action>() -> {}
            else -> { result }
        }
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