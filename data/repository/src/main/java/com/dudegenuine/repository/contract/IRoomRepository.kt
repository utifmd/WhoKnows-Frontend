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

    val currentUserId: () -> String
    val saveInClipboard: (String, String) -> Unit
    val getterOnboard: IBoarding.Getter
    val setterOnboard: IBoarding.Setter

    companion object {
        const val NOT_FOUND = "Room not found."
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

}