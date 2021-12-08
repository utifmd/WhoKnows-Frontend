package com.dudegenuine.remote.service.contract

import com.dudegenuine.remote.BuildConfig
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.RoomEntity

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IRoomService {
    suspend fun create(entity: RoomEntity): Response<RoomEntity>
    suspend fun read(id: String): Response<RoomEntity>
    suspend fun update(id: String, entity: RoomEntity): Response<RoomEntity>
    suspend fun delete(id: String)
    suspend fun list(page: Int, size: Int): Response<List<RoomEntity>>

    companion object {
        const val HEADER = BuildConfig.HEADER_API
        const val ENDPOINT = "/api/rooms"
    }
}