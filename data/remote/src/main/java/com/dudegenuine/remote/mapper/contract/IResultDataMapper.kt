package com.dudegenuine.remote.mapper.contract

import com.dudegenuine.model.Result
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.ResultEntity

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IResultDataMapper {
    fun asEntity(result: Result): ResultEntity
    fun asResult(entity: ResultEntity): Result
    fun asResult(response: Response<ResultEntity>): Result
    fun asResults(response: Response<List<ResultEntity>>): List<Result>
}