package com.dudegenuine.remote.mapper

import androidx.paging.PagingSource
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.ResourcePaging
import com.dudegenuine.model.common.ImageUtil.strOf
import com.dudegenuine.remote.entity.QuizEntity
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.mapper.contract.IQuizDataMapper
import com.dudegenuine.remote.mapper.contract.IUserDataMapper
import com.google.gson.Gson
import javax.inject.Inject

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
class QuizDataMapper
    @Inject constructor(
        val gson: Gson,
        private val mapperUser: IUserDataMapper): IQuizDataMapper {
    val TAG: String = strOf<QuizDataMapper>()

    override fun asEntity(quiz: Quiz.Complete): QuizEntity {
        val strAnswer = gson.toJson(quiz.answer)

        return QuizEntity(
            quiz.id, quiz.roomId, quiz.images, quiz.question,
            quiz.options, strAnswer, quiz.createdBy, quiz.createdAt, quiz.updatedAt,
            quiz.user?.let(mapperUser::asUserCensoredEntity)
        )
    }

    override fun asQuiz(entity: QuizEntity): Quiz.Complete {
        val result = Quiz.Complete(entity.quizId, entity.roomId, entity.images,
            entity.question, entity.options, null, entity.createdBy, entity.createdAt, entity.updatedAt,
            entity.user?.let(mapperUser::asUserCensored))

        val exactAnswer: Quiz.Answer.Exact = gson.fromJson(entity.answer, Quiz.Answer.Exact::class.java)

        return when(exactAnswer.type){
            strOf<Quiz.Answer.Possible.SingleChoice>() -> result.apply {
                answer = Quiz.Answer.Possible.SingleChoice(exactAnswer.answer ?: "")
            }
            strOf<Quiz.Answer.Possible.MultipleChoice>() -> result.apply {
                answer = Quiz.Answer.Possible.MultipleChoice(exactAnswer.answers ?: emptySet())
            } /*val itemType = object : TypeToken<List<String>>(){ }.type val data: List<String> = gson.fromJson(entity.answer, itemType)*/ //            strOf<PossibleAnswer.Slider>() -> {} //            strOf<PossibleAnswer.Action>() -> {}
            else -> { result }
        }
    }

    override fun asQuiz(response: Response<QuizEntity>): Quiz.Complete {
        return when(response.data){
            is QuizEntity -> asQuiz(response.data)
            else -> throw IllegalStateException()
        }
    }

    override fun asQuestions(response: Response<List<QuizEntity>>): List<Quiz.Complete> {
        return when(response.data){
            is List<*> -> {
                val questions = response.data.filterIsInstance<QuizEntity>()

                questions.map { asQuiz(it) }
            }
            else -> throw IllegalStateException()
        }
    }

    override fun asPagingSource(
        onEvent: suspend (Int) -> List<Quiz.Complete>):PagingSource<Int, Quiz.Complete> =
        ResourcePaging(onEvent)
}