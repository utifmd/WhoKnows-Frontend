package com.dudegenuine.remote.service.contract

import com.dudegenuine.remote.entity.QuizEntity
import com.dudegenuine.remote.entity.Response

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IQuizService {
    suspend fun create(entity: QuizEntity): Response<QuizEntity>
    suspend fun read(id: String): Response<QuizEntity>
    suspend fun update(id: String, entity: QuizEntity): Response<QuizEntity>
    suspend fun delete(id: String)
    suspend fun list(page: Int, size: Int): Response<List<QuizEntity>>

    companion object {
        const val API_KEY = "X-Api-Key: utif.pages.dev"
        const val CONTENT_TYPE = "Content-Type: application/json"
        const val ACCEPT = "Accept: application/json"
        const val ENDPOINT = "/api/questions"
    }
}