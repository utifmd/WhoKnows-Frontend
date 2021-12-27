package com.dudegenuine.repository

import android.util.Log
import com.dudegenuine.model.Room
import com.dudegenuine.model.validation.HttpFailureException
import com.dudegenuine.remote.mapper.contract.IRoomDataMapper
import com.dudegenuine.remote.service.contract.IRoomService
import com.dudegenuine.repository.contract.IRoomRepository
import com.dudegenuine.repository.contract.IRoomRepository.Companion.NOT_FOUND
import javax.inject.Inject

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
class RoomRepository
    @Inject constructor(
    private val service: IRoomService,
    private val mapper: IRoomDataMapper): IRoomRepository {
    private val TAG: String = javaClass.simpleName

    override suspend fun create(room: Room): Room = try { mapper.asRoom(
        service.create(mapper.asEntity(room)))
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun read(id: String): Room = try { mapper.asRoom(
        service.read(id))
    } catch (e: Exception){
        Log.d(TAG, e.message ?: "throwable") //Parameter specified as non-null is null: method kotlin.jvm.internal.Intrinsics.checkNotNullParameter, parameter description
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun update(id: String, room: Room): Room = try { mapper.asRoom(
        service.update(id, mapper.asEntity(room)))
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun delete(id: String) = try {
        service.delete(id)
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun list(page: Int, size: Int): List<Room> = try { mapper.asRooms(
        service.list(page, size))
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }
}