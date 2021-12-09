package com.dudegenuine.repository.contract

import com.dudegenuine.model.Quiz

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IQuizRepository {
    suspend fun create(quiz: Quiz): Quiz
    suspend fun read(id: String): Quiz
    suspend fun update(id: String, quiz: Quiz): Quiz
    suspend fun delete(id: String)
    suspend fun list(page: Int, size: Int): List<Quiz>

    companion object {
        const val NOT_FOUND = "Quiz not found."
    }
}