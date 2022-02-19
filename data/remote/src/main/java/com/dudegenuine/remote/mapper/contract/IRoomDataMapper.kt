package com.dudegenuine.remote.mapper.contract

import com.dudegenuine.local.entity.CurrentRoomState
import com.dudegenuine.model.Room
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.RoomEntity

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IRoomDataMapper {
    fun asEntity(room: Room): RoomEntity
    fun asRoom(entity: RoomEntity): Room
    fun asRoom(json: String): Room
    fun asRoom(response: Response<RoomEntity>): Room
    fun asRooms(response: Response<List<RoomEntity>>): List<Room>

    fun asCurrentBoarding(boardingQuiz: Room.RoomState.BoardingQuiz): CurrentRoomState
    fun asRoomBoardingQuiz(currentRoomState: CurrentRoomState): Room.RoomState.BoardingQuiz

    fun asCurrentBoardingQuiz(roomBoardingQuiz: Room.RoomState.OnBoardingState): CurrentRoomState.BoardingQuiz
    fun asRoomBoardingQuiz(boardingQuiz: CurrentRoomState.BoardingQuiz): Room.RoomState.OnBoardingState
}