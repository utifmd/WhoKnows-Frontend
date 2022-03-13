package com.dudegenuine.remote.mapper

import com.dudegenuine.model.Result
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.ResultEntity
import com.dudegenuine.remote.mapper.contract.IResultDataMapper
import com.dudegenuine.remote.mapper.contract.IUserDataMapper
import com.google.gson.Gson
import javax.inject.Inject

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
class ResultDataMapper
    @Inject constructor(
    private val mapperUser: IUserDataMapper,
    private val gson: Gson): IResultDataMapper {

    override fun asEntity(result: Result): ResultEntity {
        return ResultEntity(
            resultId = result.resultId, //participantId = result.participantId,
            roomId = result.roomId,
            userId = result.userId,
            correctQuiz = result.correctQuiz,
            wrongQuiz = result.wrongQuiz,
            score = result.score,
            createdAt = result.createdAt,
            updatedAt = result.updatedAt,
            user = result.user?.let(mapperUser::asUserCensoredEntity)
        )
    }

    override fun asResult(entity: ResultEntity): Result {
        return Result(
            resultId = entity.resultId,
            roomId = entity.roomId, //participantId = entity.participantId,
            userId = entity.userId,
            correctQuiz = entity.correctQuiz,
            wrongQuiz = entity.wrongQuiz,
            score = entity.score,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            user = entity.user?.let(mapperUser::asUserCensored))
    }

    override fun asResult(response: Response<ResultEntity>): Result {
        return when(response.data){
            is ResultEntity -> asResult(response.data)
            else -> throw IllegalStateException()
        }
    }

    override fun asResults(response: Response<List<ResultEntity>>): List<Result> {
        return when(response.data){
            is List<*> -> {
                val list = response.data.filterIsInstance<ResultEntity>()

                list.map { asResult(it) }
            }
            else -> throw IllegalStateException()
        }
    }
}