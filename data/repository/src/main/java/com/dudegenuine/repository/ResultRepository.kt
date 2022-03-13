package com.dudegenuine.repository

import com.dudegenuine.model.Result
import com.dudegenuine.remote.mapper.contract.IResultDataMapper
import com.dudegenuine.remote.service.contract.IResultService
import com.dudegenuine.repository.contract.IResultRepository
import javax.inject.Inject

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
class ResultRepository
    @Inject constructor(
    private val service: IResultService,
    private val mapper: IResultDataMapper): IResultRepository {

    override suspend fun create(user: Result): Result {
        return mapper.asResult(service.create(mapper.asEntity(user)))
    }

    override suspend fun read(id: String): Result {
        return mapper.asResult(
            service.read(id))
    }

    override suspend fun read(roomId: String, userId: String): Result {
        return mapper.asResult(service.read(roomId, userId))
    }

    override suspend fun update(id: String, user: Result): Result {
        return mapper.asResult(service.update(id, mapper.asEntity(user)))
    }

    override suspend fun delete(id: String) {
        service.delete(id)
    }

    override suspend fun delete(roomId: String, userId: String) {
        service.delete(roomId, userId)
    }

    override suspend fun list(page: Int, size: Int): List<Result> {
        return mapper.asResults(service.list(page, size))
    }
}