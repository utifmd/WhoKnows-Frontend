package com.dudegenuine.repository.contract

import androidx.paging.PagingSource
import com.dudegenuine.model.*
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IParticipantRepository {
    fun participation(participantId: String, room: Room.Complete, currentUser: User.Complete): Participation
    fun participationPage(room: Room.Complete, participantId: String): List<ParticipationPage>

    suspend fun create(participant: Participant): Participant
    suspend fun read(id: String): Participant
    suspend fun read(userId: String, roomId: String): Participant
    suspend fun update(id: String, participant: Participant): Participant
    suspend fun delete(id: String)
    suspend fun list(page: Int, size: Int): List<Participant>

    fun page(batchSize: Int): PagingSource<Int, Participant>
    fun save(participant: Participant)

    val prefs: IPrefsFactory

    companion object {
        const val NOT_FOUND = "Participant not found."
    }
}