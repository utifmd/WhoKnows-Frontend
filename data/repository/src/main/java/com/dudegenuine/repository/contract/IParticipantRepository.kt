package com.dudegenuine.repository.contract

import androidx.paging.PagingSource
import com.dudegenuine.model.Participant

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IParticipantRepository {
    suspend fun create(participant: Participant): Participant
    suspend fun read(id: String): Participant
    suspend fun update(id: String, participant: Participant): Participant
    suspend fun delete(id: String)
    suspend fun list(page: Int, size: Int): List<Participant>

    fun page(batchSize: Int): PagingSource<Int, Participant>
    fun save(participant: Participant)

    companion object {
        const val NOT_FOUND = "Participant not found."
    }
}