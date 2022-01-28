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

    companion object {
        const val NOT_FOUND = "Room not found."
    }
}