package com.dudegenuine.repository.contract

import androidx.paging.PagingSource
import com.dudegenuine.model.Room


/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IRoomRepository {
    val currentParticipant: () -> String

    companion object {
        const val NOT_FOUND = "Room not found."
        const val CURRENT_PARTICIPANT_ID = "current_participant_id"
    }

    suspend fun create(room: Room): Room
    suspend fun read(id: String): Room
    suspend fun update(id: String, room: Room): Room
    suspend fun delete(id: String)
    suspend fun list(page: Int, size: Int): List<Room>
    suspend fun list(userId: String): List<Room>

    suspend fun load(participantId: String? = null): Room.RoomState.BoardingQuiz
    suspend fun save(boarding: Room.RoomState.BoardingQuiz)
    suspend fun replace(boarding: Room.RoomState.BoardingQuiz)
    suspend fun unload()/*(participantId: String)*/

    fun page(batchSize: Int): PagingSource<Int, Room>

    val currentToken: () -> String
    val currentUserId: () -> String
    val currentRunningTime: () -> String
    val setClipboard: (String, String) -> Unit
}