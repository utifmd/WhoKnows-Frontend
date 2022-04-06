package com.dudegenuine.repository.contract

import android.content.BroadcastReceiver
import androidx.paging.PagingSource
import com.dudegenuine.model.Room


/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IRoomRepository {
    companion object {
        const val NOT_FOUND = "Room boarding not found."
    }

    suspend fun create(room: Room.Complete): Room.Complete
    suspend fun read(id: String): Room.Complete
    suspend fun update(id: String, room: Room.Complete): Room.Complete
    suspend fun delete(id: String)
    suspend fun listComplete(page: Int, size: Int): List<Room.Complete>
    suspend fun listCensored(page: Int, size: Int): List<Room.Censored>
    suspend fun listComplete(userId: String, page: Int, size: Int): List<Room.Complete>

    suspend fun load(): Room.State.BoardingQuiz
    suspend fun save(boarding: Room.State.BoardingQuiz)
    suspend fun replace(boarding: Room.State.BoardingQuiz)
    suspend fun unload()

    fun page(batchSize: Int): PagingSource<Int, Room.Censored>
    fun page(userId: String, batchSize: Int): PagingSource<Int, Room.Complete>

    val setClipboard: (String, String) -> Unit
    val timerReceived: ((Double, Boolean) -> Unit) -> BroadcastReceiver
}