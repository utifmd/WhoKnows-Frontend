package com.dudegenuine.remote.mapper

import com.dudegenuine.model.Result
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.ResultEntity
import com.dudegenuine.remote.mapper.contract.IResultDataMapper

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
class ResultDataMapper: IResultDataMapper {

    override fun asEntity(result: Result): ResultEntity {
        return ResultEntity(result.id, result.participantId, result.roomId, result.userId,
            result.correctQuiz, result.wrongQuiz, result.score, result.createdAt, result.updatedAt)
    }

    override fun asResult(entity: ResultEntity): Result {
        return Result(entity.id, entity.participantId, entity.roomId, entity.userId,
            entity.correctQuiz, entity.wrongQuiz, entity.score, entity.createdAt, entity.updatedAt)
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