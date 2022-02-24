package com.dudegenuine.repository

import androidx.paging.PagingSource
import com.dudegenuine.model.Quiz
import com.dudegenuine.remote.mapper.contract.IQuizDataMapper
import com.dudegenuine.remote.service.contract.IQuizService
import com.dudegenuine.repository.contract.IQuizRepository
import javax.inject.Inject

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
class QuizRepository
    @Inject constructor(
    private val service: IQuizService,
    private val mapper: IQuizDataMapper): IQuizRepository {

    override suspend fun create(quiz: Quiz): Quiz =
        mapper.asQuiz(service.create(mapper.asEntity(quiz)))

    override suspend fun read(id: String): Quiz =
        mapper.asQuiz(service.read(id))

    override suspend fun update(id: String, quiz: Quiz): Quiz =
        mapper.asQuiz(service.update(id, mapper.asEntity(quiz)))

    override suspend fun delete(id: String) =
        service.delete(id)

    override suspend fun list(page: Int, size: Int): List<Quiz> =
        mapper.asQuestions(service.list(page, size))

    override fun page(batchSize: Int): PagingSource<Int, Quiz> =
        mapper.asPagingSource { pageNumber ->
            list(pageNumber, batchSize)
        }
}