package com.dudegenuine.remote.service.contract

import com.dudegenuine.remote.entity.ImpressionEntity
import com.dudegenuine.remote.entity.Response

/**
 * Wed, 06 Jul 2022
 * WhoKnows by utifmd
 **/
interface IImpressionService {
    suspend fun create(entity: ImpressionEntity): Response<ImpressionEntity>
    suspend fun update(id: String, entity: ImpressionEntity): Response<ImpressionEntity>

    companion object {
        const val API_KEY = "X-Api-Key: utif.pages.dev"
        const val CONTENT_TYPE = "Content-Type: application/json"
        const val ACCEPT = "Accept: application/json"
        const val ENDPOINT = "/api/impressions"
    }
}