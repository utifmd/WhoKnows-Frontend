package com.dudegenuine.repository.contract

import com.dudegenuine.model.Result

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IResultRepository {
    suspend fun create(user: Result): Result
    suspend fun read(id: String): Result
    suspend fun read(roomId: String, userId: String): Result
    suspend fun update(id: String, user: Result): Result
    suspend fun delete(id: String)
    suspend fun delete(roomId: String, userId: String)
    suspend fun list(page: Int, size: Int): List<Result>

    companion object {
        const val NOT_FOUND = "Result not found."
    }
}