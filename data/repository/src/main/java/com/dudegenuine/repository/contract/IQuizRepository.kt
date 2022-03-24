package com.dudegenuine.repository.contract

import androidx.paging.PagingSource
import com.dudegenuine.model.Quiz

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IQuizRepository {
    suspend fun create(quiz: Quiz.Complete): Quiz.Complete
    suspend fun read(id: String): Quiz.Complete
    suspend fun update(id: String, quiz: Quiz.Complete): Quiz.Complete
    suspend fun delete(id: String)
    suspend fun list(page: Int, size: Int): List<Quiz.Complete>

    fun page(batchSize: Int): PagingSource<Int, Quiz.Complete>

    companion object {
        const val NOT_FOUND = "Quiz not found."
    }
}