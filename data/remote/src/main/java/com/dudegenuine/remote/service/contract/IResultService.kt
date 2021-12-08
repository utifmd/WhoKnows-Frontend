package com.dudegenuine.remote.service.contract

import com.dudegenuine.remote.BuildConfig
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.ResultEntity

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IResultService {
    suspend fun create(entity: ResultEntity): Response<ResultEntity>
    suspend fun read(id: String): Response<ResultEntity>
    suspend fun update(id: String, entity: ResultEntity): Response<ResultEntity>
    suspend fun delete(id: String)
    suspend fun list(page: Int, size: Int): Response<List<ResultEntity>>

    companion object {
        const val HEADER = BuildConfig.HEADER_API
        const val ENDPOINT = "/api/results"
    }
}