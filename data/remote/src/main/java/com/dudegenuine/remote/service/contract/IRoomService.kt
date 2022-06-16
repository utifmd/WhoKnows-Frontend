package com.dudegenuine.remote.service.contract

import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.RoomEntity

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IRoomService {
    suspend fun create(entity: RoomEntity.Complete): Response<RoomEntity.Complete>
    suspend fun read(id: String): Response<RoomEntity.Complete>
    suspend fun update(id: String, entity: RoomEntity.Complete): Response<RoomEntity.Complete>
    suspend fun delete(id: String)
    suspend fun listComplete(page: Int, size: Int): Response<List<RoomEntity.Complete>>
    suspend fun listCensored(page: Int, size: Int): Response<List<RoomEntity.Censored>>
    suspend fun listComplete(userId: String, page: Int, size: Int): Response<List<RoomEntity.Complete>>
    suspend fun listCensoredSearched(query: String, page: Int, size: Int): Response<List<RoomEntity.Censored>>

    companion object {
        const val API_KEY = "X-Api-Key: utif.pages.dev"
        const val CONTENT_TYPE = "Content-Type: application/json"
        const val ACCEPT = "Accept: application/json"
        const val ENDPOINT = "/api/rooms"
    }
}