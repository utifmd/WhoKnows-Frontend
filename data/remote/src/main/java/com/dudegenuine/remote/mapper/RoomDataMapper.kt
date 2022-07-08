package com.dudegenuine.remote.mapper

import android.util.Log
import androidx.paging.PagingSource
import com.dudegenuine.local.entity.ParticipationPageTable
import com.dudegenuine.local.entity.ParticipationTable
import com.dudegenuine.local.entity.QuizTable
import com.dudegenuine.local.entity.RoomCensoredTable
import com.dudegenuine.model.*
import com.dudegenuine.model.common.ImageUtil.strOf
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.RoomEntity
import com.dudegenuine.remote.mapper.contract.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
class RoomDataMapper
    @Inject constructor(
    private val currentUserId: String,
    private val gson: Gson,
    private val mapperUser: IUserDataMapper,
    private val mapperQuiz: IQuizDataMapper,
    private val mapperImpression: IImpressionDataMapper,
    private val mapperParticipant: IParticipantDataMapper): IRoomDataMapper {
    private val TAG: String = javaClass.simpleName

    init {
        Log.d(TAG, "currentUserId: $currentUserId")
    }

    override fun asEntity(room: Room.Complete): RoomEntity.Complete {
        return RoomEntity.Complete(
            roomId = room.id,
            userId = room.userId,
            minute = room.minute,
            title = room.title,
            token = room.token,
            description = room.description,
            expired = room.expired,
            private = room.private,
            createdAt = room.createdAt,
            updatedAt = room.updatedAt,
            impressions = room.impressions.map(mapperImpression::asEntity),
            user = room.user
                ?.let(mapperUser::asUserCensoredEntity),
            questions = room.questions
                .map(mapperQuiz::asEntity),
            participants = room.participants
                .map(mapperParticipant::asEntity),
        )
    }

    override fun asRoom(entity: RoomEntity.Complete): Room.Complete {

        return Room.Complete(
            id = entity.roomId,
            userId = entity.userId,
            minute = entity.minute,
            title = entity.title,
            token = entity.token,
            description = entity.description,
            expired = entity.expired,
            private = entity.private ?: false,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,

            isOwner = entity.userId == currentUserId,
            impressions = entity.impressions.map(mapperImpression::asImpression),
            impression = entity.impressions.map(mapperImpression::asImpression)
                .find{ it.userId == currentUserId },
            impressionSize = entity.impressions.count{ it.good },
            hasImpressedBefore = entity.impressions
                .any{ it.userId == currentUserId },
            impressed = entity.impressions
                .any{ it.userId == currentUserId && it.good },

            user = entity.user?.let(mapperUser::asUserCensored),
            questions = entity.questions
                .map { mapperQuiz.asQuiz(it) },
            participants = entity.participants
                .map { mapperParticipant.asParticipant(it) }//.filter { it.expired }
        )
    }

    override fun asRoom(json: String): Room.Complete = gson.fromJson(json, Room.Complete::class.java)

    override fun asRoom(response: Response<RoomEntity.Complete>): Room.Complete {
        return when(response.data){
            is RoomEntity.Complete -> asRoom(response.data)
            else -> throw IllegalStateException()
        }
    }

    override fun asRooms(response: Response<List<RoomEntity.Complete>>): List<Room.Complete> {
        return when(response.data){
            is List<*> -> {
                val entities = response.data
                    .filterIsInstance<RoomEntity.Complete>()

                entities.map { asRoom(it) }
            }
            else -> throw IllegalStateException()
        }
    }

    override fun asParticipationTable(participation: Participation): ParticipationTable {
        return ParticipationTable(
            participantId = participation.participantId,
            user = participation.user,
            userId = participation.userId,
            roomId = participation.roomId,
            roomTitle = participation.roomTitle,
            roomDesc = participation.roomDesc,
            roomMinute = participation.roomMinute,
            currentQuestionIdx = participation.currentQuestionIdx,
            pages = participation.pages.map(::asParticipationPageTable)
        )
    }

    override fun asParticipationPageTable(participationPage: ParticipationPage): ParticipationPageTable {
        return ParticipationPageTable(
            quiz = asQuizTable(participationPage.quiz),
            questionIndex = participationPage.questionIndex,
            totalQuestionsCount = participationPage.totalQuestionsCount,
            showPrevious = participationPage.showPrevious,
            showDone = participationPage.showDone,
            answer = participationPage.answer,
            enableNext = participationPage.enableNext,
            isCorrect = participationPage.isCorrect
        )
    }

    override fun asParticipation(table: ParticipationTable): Participation {
        return Participation(
            participantId = table.participantId,
            user = table.user,
            userId = table.userId,
            roomId = table.roomId,
            roomTitle = table.roomTitle,
            roomDesc = table.roomDesc,
            roomMinute = table.roomMinute,
            pages = table.pages.map(::asParticipationPage)).apply {

            currentQuestionIdx = table.currentQuestionIdx
        }
    }

    override fun asParticipationPage(table: ParticipationPageTable): ParticipationPage {
        return ParticipationPage(
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

    override fun asRoomCensoredEntity(room: Room.Censored): RoomEntity.Censored {
        return RoomEntity.Censored(
            roomId = room.roomId,
            userId = room.userId,
            minute = room.minute,
            title = room.title,
            token = room.token,
            description = room.description,
            expired = room.expired,
            private = room.private,
            user = room.user?.let(mapperUser::asUserCensoredEntity),
            questionSize = room.questionSize,
            participantSize = room.participantSize,
            impressions = emptyList(),
        )
    }

    override fun asRoomCensored(entity: RoomEntity.Censored): Room.Censored {
        return Room.Censored(
            roomId = entity.roomId,
            userId = entity.userId,
            minute = entity.minute,
            title = entity.title,
            token = entity.token,
            description = entity.description,
            expired = entity.expired,
            user = entity.user?.let(mapperUser::asUserCensored),
            questionSize = entity.questionSize,
            participantSize = entity.participantSize,
            private = entity.private ?: false,

            isOwner = entity.userId == currentUserId,
            impressions = entity.impressions.map(mapperImpression::asImpression),
            impression = entity.impressions.map(mapperImpression::asImpression)
                .find{ it.userId == currentUserId },
            impressionSize = entity.impressions.count{ it.good },
            hasImpressedBefore = entity.impressions
                .any{ it.userId == currentUserId },
            impressed = entity.impressions
                .any{ it.userId == currentUserId && it.good }
        )
    }

    override fun asRoomsCensored(response: Response<List<RoomEntity.Censored>>): List<Room.Censored> {
        return when(response.data){
            is List<*> -> {
                val entities = response.data
                    .filterIsInstance<RoomEntity.Censored>()

                entities.map { asRoomCensored(it) }
            }
            else -> throw IllegalStateException()
        }
    }

    override fun asRoomCensored(tableRoom: RoomCensoredTable): Room.Censored {
        return Room.Censored(
            roomId = tableRoom.roomId,
            userId = tableRoom.userId,
            minute = tableRoom.minute,
            title = tableRoom.title,
            token = tableRoom.token,
            description = tableRoom.description,
            expired = tableRoom.expired,
            user = tableRoom.user,
            questionSize = tableRoom.questionSize,
            participantSize = tableRoom.participantSize,
            private = tableRoom.privation,

            isOwner = tableRoom.isOwner,
            impressions = tableRoom.impressions,
            impression = tableRoom.impressions.find{ it.userId == currentUserId },
            impressionSize = tableRoom.impressions.count{ it.good },
            impressed = tableRoom.impressions.any{ it.userId == currentUserId && it.good },
            hasImpressedBefore = tableRoom.impressions.any{ it.userId == currentUserId },
        )
    }
    override fun asRoomsCensored(list: List<RoomCensoredTable>):
            List<Room.Censored> = list.map(::asRoomCensored)

    override fun asPagingCompleteSource(
        onEvent: suspend (Int) -> List<Room.Complete>): PagingSource<Int, Room.Complete> =
        try { ResourcePaging(onEvent) } catch (e: Exception) {
            ResourcePaging { emptyList() }
        }

    override fun asPagingCensoredSource(
        onEvent: suspend (Int) -> List<Room.Censored>): PagingSource<Int, Room.Censored> =
        try { ResourcePaging(onEvent) } catch (e: Exception) {
            ResourcePaging { emptyList() }
        }
}