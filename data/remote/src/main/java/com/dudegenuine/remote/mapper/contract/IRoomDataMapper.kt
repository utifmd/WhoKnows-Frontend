package com.dudegenuine.remote.mapper.contract

import androidx.paging.PagingSource
import com.dudegenuine.local.entity.BoardingQuizTable
import com.dudegenuine.local.entity.OnBoardingStateTable
import com.dudegenuine.local.entity.QuizTable
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.Room
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.RoomEntity

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IRoomDataMapper {
    fun asEntity(room: Room.Complete): RoomEntity.Complete
    fun asRoom(entity: RoomEntity.Complete): Room.Complete
    fun asRoom(json: String): Room.Complete
    fun asRoom(response: Response<RoomEntity.Complete>): Room.Complete
    fun asRooms(response: Response<List<RoomEntity.Complete>>): List<Room.Complete>

    fun asBoardingQuizTable(boarding: Room.State.BoardingQuiz): BoardingQuizTable
    fun asBoardingQuiz(table: BoardingQuizTable): Room.State.BoardingQuiz

    fun asOnBoardingStateTable(boarding: Room.State.OnBoardingState): OnBoardingStateTable
    fun asOnBoardingState(table: OnBoardingStateTable): Room.State.OnBoardingState

    fun asQuizTable(quiz: Quiz.Complete): QuizTable
    fun asQuiz(table: QuizTable): Quiz.Complete

    fun asRoomCensoredEntity(room: Room.Censored): RoomEntity.Censored
    fun asRoomCensored(entity: RoomEntity.Censored): Room.Censored
    fun asRoomsCensored(response: Response<List<RoomEntity.Censored>>): List<Room.Censored>

    fun asPagingCompleteSource(onEvent: suspend (Int) -> List<Room.Complete>): PagingSource<Int, Room.Complete>
    fun asPagingCensoredSource(onEvent: suspend (Int) -> List<Room.Censored>): PagingSource<Int, Room.Censored>
}