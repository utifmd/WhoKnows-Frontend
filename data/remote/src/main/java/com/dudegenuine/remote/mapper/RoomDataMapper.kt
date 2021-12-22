package com.dudegenuine.remote.mapper

import com.dudegenuine.model.Participant
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.Room
import com.dudegenuine.remote.entity.ParticipantEntity
import com.dudegenuine.remote.entity.QuizEntity
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.RoomEntity
import com.dudegenuine.remote.mapper.contract.IRoomDataMapper

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
class RoomDataMapper: IRoomDataMapper {
    private val TAG: String = javaClass.simpleName

    override fun asEntity(room: Room): RoomEntity {
        return RoomEntity(
            room.id,
            room.userId,
            room.minute,
            room.title,
            room.description,
            room.expired,
            room.createdAt,
            room.updatedAt,
            room.questions?.filterIsInstance<QuizEntity>() ?: emptyList(),
            room.participants?.filterIsInstance<ParticipantEntity>() ?: emptyList(),
        )
    }

    override fun asRoom(entity: RoomEntity): Room {
        return Room(
            entity.id,
            entity.userid,
            entity.minute,
            entity.title,
            entity.description,
            entity.expired,
            entity.createdAt,
            entity.updatedAt,
            entity.questions
                .filterIsInstance<Quiz>(),
            entity.participants
                .filterIsInstance<Participant>()
        )
    }

    override fun asRoom(response: Response<RoomEntity>): Room {
        return when(response.data){
            is RoomEntity -> asRoom(response.data)
            else -> throw IllegalStateException()
        }
    }

    override fun asRooms(response: Response<List<RoomEntity>>): List<Room> {
        return when(response.data){
            is List<*> -> {
                val entities = response.data
                    .filterIsInstance<RoomEntity>()

                entities.map { asRoom(it) }
            }
            else -> throw IllegalStateException()
        }
    }
}