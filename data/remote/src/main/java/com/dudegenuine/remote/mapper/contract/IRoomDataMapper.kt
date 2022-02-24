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
    fun asEntity(room: Room): RoomEntity
    fun asRoom(entity: RoomEntity): Room
    fun asRoom(json: String): Room
    fun asRoom(response: Response<RoomEntity>): Room
    fun asRooms(response: Response<List<RoomEntity>>): List<Room>

    fun asBoardingQuizTable(boarding: Room.RoomState.BoardingQuiz): BoardingQuizTable
    fun asBoardingQuiz(table: BoardingQuizTable): Room.RoomState.BoardingQuiz

    fun asOnBoardingStateTable(boarding: Room.RoomState.OnBoardingState): OnBoardingStateTable
    fun asOnBoardingState(table: OnBoardingStateTable): Room.RoomState.OnBoardingState

    fun asQuizTable(quiz: Quiz): QuizTable
    fun asQuiz(table: QuizTable): Quiz

    fun asPagingSource(onEvent: suspend (Int) -> List<Room>): PagingSource<Int, Room>
}