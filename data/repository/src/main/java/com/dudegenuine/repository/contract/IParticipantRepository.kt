package com.dudegenuine.repository.contract

import com.dudegenuine.model.Participant

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IParticipantRepository {
    suspend fun create(user: Participant): Participant
    suspend fun read(id: String): Participant
    suspend fun update(id: String, user: Participant): Participant
    suspend fun delete(id: String)
    suspend fun list(page: Int, size: Int): List<Participant>

    companion object {
        const val NOT_FOUND = "Participant not found."
    }
}