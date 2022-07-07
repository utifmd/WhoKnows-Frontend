package com.dudegenuine.remote.service.contract

import com.dudegenuine.remote.entity.ParticipantEntity
import com.dudegenuine.remote.entity.Response

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IParticipantService {
    suspend fun create(entity: ParticipantEntity): Response<ParticipantEntity>
    suspend fun read(id: String): Response<ParticipantEntity>
    suspend fun read(userId: String, roomId: String): Response<ParticipantEntity>
    suspend fun update(id: String, entity: ParticipantEntity): Response<ParticipantEntity>
    suspend fun delete(id: String)
    suspend fun list(page: Int, size: Int): Response<List<ParticipantEntity>>

    companion object {
        const val API_KEY = "X-Api-Key: utif.pages.dev"
        const val CONTENT_TYPE = "Content-Type: application/json"
        const val ACCEPT = "Accept: application/json"
        const val ENDPOINT = "/api/participants"
    }
}