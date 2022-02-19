package com.dudegenuine.repository.contract

import com.dudegenuine.model.Room


/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IRoomRepository {
    suspend fun create(room: Room): Room
    suspend fun read(id: String): Room
    suspend fun update(id: String, room: Room): Room
    suspend fun delete(id: String)
    suspend fun list(page: Int, size: Int): List<Room>
    suspend fun list(userId: String): List<Room>

    val currentParticipant: () -> String

    suspend fun load(participantId: String? = null): Room.RoomState.BoardingQuiz
    suspend fun save(boarding: Room.RoomState.BoardingQuiz)
    suspend fun replace(boarding: Room.RoomState.BoardingQuiz)
    suspend fun unload(participantId: String)

    companion object {
        const val NOT_FOUND = "Room not found."
        const val CURRENT_PARTICIPANT_ID = "current_participant_id"
    }

    sealed interface IBoarding {
        interface Getter: IBoarding {
            val roomId: () -> String
            val participantId: () -> String
        }

        interface Setter: IBoarding {
            fun roomId(id: String)
            fun participantId(id: String)
        }
    }

    val currentToken: () -> String
    val currentUserId: () -> String
    val setClipboard: (String, String) -> Unit

    val getterOnboard: IBoarding.Getter
    val setterOnboard: IBoarding.Setter
}