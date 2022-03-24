package com.dudegenuine.remote.mapper

import androidx.paging.PagingSource
import com.dudegenuine.local.entity.BoardingQuizTable
import com.dudegenuine.local.entity.OnBoardingStateTable
import com.dudegenuine.local.entity.QuizTable
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.ResourcePaging
import com.dudegenuine.model.Room
import com.dudegenuine.model.common.ImageUtil.strOf
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.RoomCensoredEntity
import com.dudegenuine.remote.entity.RoomEntity
import com.dudegenuine.remote.mapper.contract.IParticipantDataMapper
import com.dudegenuine.remote.mapper.contract.IQuizDataMapper
import com.dudegenuine.remote.mapper.contract.IRoomDataMapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

    override fun asEntity(room: Room.Complete): RoomEntity {
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

    override fun asRoom(entity: RoomEntity): Room.Complete {
        return Room.Complete(
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

    override fun asRoom(json: String): Room.Complete = gson.fromJson(json, Room.Complete::class.java)

    override fun asRoom(response: Response<RoomEntity>): Room.Complete {
        return when(response.data){
            is RoomEntity -> asRoom(response.data)
            else -> throw IllegalStateException()
        }
    }

    override fun asRooms(response: Response<List<RoomEntity>>): List<Room.Complete> {
        return when(response.data){
            is List<*> -> {
                val entities = response.data
                    .filterIsInstance<RoomEntity>()

                entities.map { asRoom(it) }
            }
            else -> throw IllegalStateException()
        }
    }

    override fun asBoardingQuizTable(
        boarding: Room.State.BoardingQuiz): BoardingQuizTable {

        return BoardingQuizTable(
            participantId = boarding.participantId,
            participant = boarding.participant,
            userId = boarding.userId,
            roomId = boarding.roomId,
            roomTitle = boarding.roomTitle,
            roomDesc = boarding.roomDesc,
            roomMinute = boarding.roomMinute,
            currentQuestionIdx = boarding.currentQuestionIdx,
            quizzes = boarding.quizzes
                .map(::asOnBoardingStateTable)
        )
    }

    override fun asOnBoardingStateTable(boarding: Room.State.OnBoardingState): OnBoardingStateTable {
        return OnBoardingStateTable(
            quiz = asQuizTable(boarding.quiz),
            questionIndex = boarding.questionIndex,
            totalQuestionsCount = boarding.totalQuestionsCount,
            showPrevious = boarding.showPrevious,
            showDone = boarding.showDone,
            answer = boarding.answer,
            enableNext = boarding.enableNext,
            isCorrect = boarding.isCorrect
        )
    }

    override fun asBoardingQuiz(table: BoardingQuizTable): Room.State.BoardingQuiz {
        return Room.State.BoardingQuiz(
            participantId = table.participantId,
            participant = table.participant,
            userId = table.userId,
            roomId = table.roomId,
            roomTitle = table.roomTitle,
            roomDesc = table.roomDesc,
            roomMinute = table.roomMinute,
            quizzes = table.quizzes
                .map(::asOnBoardingState)).apply {

            currentQuestionIdx = table.currentQuestionIdx
        }
    }

    override fun asOnBoardingState(table: OnBoardingStateTable): Room.State.OnBoardingState {
        return Room.State.OnBoardingState(
            quiz = asQuiz(table.quiz),
            questionIndex = table.questionIndex,
            totalQuestionsCount = table.totalQuestionsCount,
            showPrevious = table.showPrevious,
            showDone = table.showDone).apply {

            answer = table.answer
            enableNext = table.enableNext
            isCorrect = table.isCorrect
        }
    }

    override fun asQuizTable(quiz: Quiz.Complete): QuizTable {
        val strAnswer = gson.toJson(quiz.answer)

        return QuizTable(
            id = quiz.id,
            roomId = quiz.roomId,
            images = quiz.images,
            question = quiz.question,
            options = quiz.options,
            answer = strAnswer,
            createdBy = quiz.createdBy,
            createdAt = quiz.createdAt,
            updatedAt = quiz.updatedAt,
            user = quiz.user,
        )
    }

    override fun asQuiz(table: QuizTable): Quiz.Complete {
        val result = Quiz.Complete(
            id = table.id,
            roomId = table.roomId,
            images = table.images,
            question = table.question,
            options = table.options,
            answer = null,
            createdBy = table.createdBy,
            createdAt = table.createdAt,
            updatedAt = table.updatedAt,
            user = table.user,
        )

        val type = object: TypeToken<Quiz.Answer.Exact?>(){}.type
        val exactAnswer: Quiz.Answer.Exact = gson.fromJson(table.answer, type)

        return when(exactAnswer.type){
            strOf<Quiz.Answer.Possible.SingleChoice>() -> result.apply {
                answer = Quiz.Answer.Possible.SingleChoice(exactAnswer.answer ?: "")
            }
            strOf<Quiz.Answer.Possible.MultipleChoice>() -> result.apply {
                answer = Quiz.Answer.Possible.MultipleChoice(exactAnswer.answers ?: emptySet())
            } //val itemType = object : TypeToken<List<String>>(){ }.type val data: List<String> = gson.fromJson(entity.answer, itemType) //            strOf<PossibleAnswer.Slider>() -> {} //            strOf<PossibleAnswer.Action>() -> {}
            else -> { result }
        }
    }

    override fun asRoomCensoredEntity(room: Room.Censored): RoomCensoredEntity {
        return RoomCensoredEntity(
            roomId = room.roomId,
            userId = room.userId,
            minute = room.minute,
            title = room.title,
            description = room.description,
            expired = room.expired,
        )
    }

    override fun asRoomCensored(entity: RoomCensoredEntity): Room.Censored {
        return Room.Censored(
            roomId = entity.roomId,
            userId = entity.userId,
            minute = entity.minute,
            title = entity.title,
            description = entity.description,
            expired = entity.expired,
        )
    }

    override fun asPagingSource(
        onEvent: suspend (Int) -> List<Room.Complete>): PagingSource<Int, Room.Complete> =
        ResourcePaging(onEvent)
}