package com.dudegenuine.remote.service.contract

import com.dudegenuine.remote.BuildConfig
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.ParticipantEntity

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IParticipantService {
    suspend fun create(entity: ParticipantEntity): Response<ParticipantEntity>
    suspend fun read(id: String): Response<ParticipantEntity>
    suspend fun update(id: String, entity: ParticipantEntity): Response<ParticipantEntity>
    suspend fun delete(id: String)
    suspend fun list(page: Int, size: Int): Response<List<ParticipantEntity>>

    companion object {
        const val HEADER = BuildConfig.HEADER_API
        const val ENDPOINT = "/api/participants"
    }
}