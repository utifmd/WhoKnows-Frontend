package com.dudegenuine.repository

import com.dudegenuine.model.Result
import com.dudegenuine.model.validation.HttpFailureException
import com.dudegenuine.remote.mapper.contract.IResultDataMapper
import com.dudegenuine.remote.service.contract.IResultService
import com.dudegenuine.repository.contract.IResultRepository
import com.dudegenuine.repository.contract.IResultRepository.Companion.NOT_FOUND
import javax.inject.Inject

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
class ResultRepository
    @Inject constructor(
    private val service: IResultService,
    private val mapper: IResultDataMapper): IResultRepository {

    override suspend fun create(user: Result): Result = try { mapper.asResult(
        service.create(mapper.asEntity(user)))
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun read(id: String): Result = try { mapper.asResult(
        service.read(id))
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun update(id: String, user: Result): Result = try { mapper.asResult(
        service.update(id, mapper.asEntity(user)))
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun delete(id: String) = try {
        service.delete(id)
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun list(page: Int, size: Int): List<Result> = try { mapper.asResults(
        service.list(page, size))
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }
}