package com.dudegenuine.remote.mapper

import com.dudegenuine.local.entity.CurrentRoomState
import com.dudegenuine.model.Room
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.RoomEntity
import com.dudegenuine.remote.mapper.contract.IParticipantDataMapper
import com.dudegenuine.remote.mapper.contract.IQuizDataMapper
import com.dudegenuine.remote.mapper.contract.IRoomDataMapper
import com.google.gson.Gson
import javax.inject.Inject

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
class RoomDataMapper
    @Inject constructor(
    private val gson: Gson,
    private val mapperQuiz: IQuizDataMapper,
    private val mapperParticipant: IParticipantDataMapper): IRoomDataMapper {
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
            room.questions
                .map { mapperQuiz.asEntity(it) },
            room.participants
                .map { mapperParticipant.asEntity(it) },
        )
    }

    override fun asRoom(entity: RoomEntity): Room {
        return Room(
            entity.roomId,
            entity.userid,
            entity.minute,
            entity.title,
            entity.description,
            entity.expired,
            entity.createdAt,
            entity.updatedAt,
            entity.questions
                .map { mapperQuiz.asQuiz(it) },
            entity.participants
                .map { mapperParticipant.asParticipant(it) }
        )
    }

    override fun asRoom(json: String): Room = gson.fromJson(json, Room::class.java)

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

    override fun asCurrentBoarding(boardingQuiz: Room.RoomState.BoardingQuiz): CurrentRoomState {
        return CurrentRoomState(
            participantId = boardingQuiz.participantId,
            participantName = boardingQuiz.participantName,
            roomId = boardingQuiz.roomId,
            roomTitle = boardingQuiz.roomTitle,
            roomDesc = boardingQuiz.roomDesc,
            roomMinute = boardingQuiz.roomMinute,
            currentQuestionIdx = boardingQuiz.currentQuestionIdx,
            quizzes = boardingQuiz.quizzes
                .map(::asCurrentBoardingQuiz)
        )
    }

    override fun asRoomBoardingQuiz(currentRoomState: CurrentRoomState): Room.RoomState.BoardingQuiz {
        return Room.RoomState.BoardingQuiz(
            participantId = currentRoomState.participantId,
            participantName = currentRoomState.participantName,
            roomId = currentRoomState.roomId,
            roomTitle = currentRoomState.roomTitle,
            roomDesc = currentRoomState.roomDesc,
            roomMinute = currentRoomState.roomMinute,
            quizzes = currentRoomState.quizzes
                .map(::asRoomBoardingQuiz)
        )
    }

    override fun asRoomBoardingQuiz(boardingQuiz: CurrentRoomState.BoardingQuiz): Room.RoomState.OnBoardingState {
        return Room.RoomState.OnBoardingState(
            quiz = boardingQuiz.quiz,
            questionIndex = boardingQuiz.questionIndex,
            totalQuestionsCount = boardingQuiz.totalQuestionsCount,
            showPrevious = boardingQuiz.showPrevious,
            showDone = boardingQuiz.showDone,
        )
    }

    override fun asCurrentBoardingQuiz(roomBoardingQuiz: Room.RoomState.OnBoardingState): CurrentRoomState.BoardingQuiz {
        return CurrentRoomState.BoardingQuiz(
            quiz = roomBoardingQuiz.quiz,
            questionIndex = roomBoardingQuiz.questionIndex,
            totalQuestionsCount = roomBoardingQuiz.totalQuestionsCount,
            showPrevious = roomBoardingQuiz.showPrevious,
            showDone = roomBoardingQuiz.showDone,
        )
    }
}