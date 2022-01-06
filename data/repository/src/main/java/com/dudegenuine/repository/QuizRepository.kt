package com.dudegenuine.repository

import com.dudegenuine.model.Quiz
import com.dudegenuine.model.common.ImageUtil.strOf
import com.dudegenuine.model.validation.HttpFailureException
import com.dudegenuine.remote.mapper.contract.IQuizDataMapper
import com.dudegenuine.remote.service.contract.IQuizService
import com.dudegenuine.repository.contract.IQuizRepository
import com.dudegenuine.repository.contract.IQuizRepository.Companion.NOT_FOUND
import javax.inject.Inject

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
class QuizRepository
    @Inject constructor(
    private val service: IQuizService,
    private val mapper: IQuizDataMapper): IQuizRepository {
    private val TAG: String = strOf<QuizRepository>()

    override suspend fun create(quiz: Quiz): Quiz = try {
        mapper.asQuiz(service.create(mapper.asEntity(quiz)))
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun read(id: String): Quiz = try { mapper.asQuiz(
        service.read(id))
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun update(id: String, quiz: Quiz): Quiz = try { mapper.asQuiz(
        service.update(id, mapper.asEntity(quiz)))
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun delete(id: String) = try {
        service.delete(id)
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun list(page: Int, size: Int): List<Quiz> = try { mapper.asQuestions(
        service.list(page, size))
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }
}