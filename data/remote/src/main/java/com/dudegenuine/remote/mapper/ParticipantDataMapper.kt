package com.dudegenuine.remote.mapper

import androidx.paging.PagingSource
import com.dudegenuine.model.*
import com.dudegenuine.remote.entity.ParticipantEntity
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.mapper.contract.IParticipantDataMapper
import com.dudegenuine.remote.mapper.contract.IUserDataMapper
import javax.inject.Inject

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
class ParticipantDataMapper
    @Inject constructor(
    private val currentUserId: String,
    private val mapperUser: IUserDataMapper): IParticipantDataMapper {
    private val TAG: String = javaClass.simpleName
    override fun asAnParticipation(
        participantId: String, room: Room.Complete, pages: List<ParticipationPage>,
        participant: User.Censored): Participation {
        return Participation(
            participantId = participantId,
            userId = room.userId,
            roomId = room.id,
            roomTitle = room.title,
            roomDesc = room.description,
            roomMinute = room.minute,
            roomToken = room.token,
            roomRecipientIds = room.participantIds,
            /*roomIsParticipant = room.isParticipant,
            roomIsParticipated = room.isParticipated,*/
            pages = pages,
            user = participant
        )
    }

    override fun asParticipationPage(
        index: Int, total: Int, question: Quiz.Complete): ParticipationPage {
        return ParticipationPage(
            quiz = question,
            questionIndex = index +1,
            totalQuestionsCount = total,
            showPrevious = index > 0,
            showDone = index == total -1
        )
    }

    override fun asEntity(participant: Participant): ParticipantEntity {
        return ParticipantEntity(
            participant.id,
            participant.roomId,
            participant.userId,
            participant.currentPage,
            participant.timeLeft,
            participant.expired,
            participant.createdAt,
            participant.updatedAt, //, participant.results.filterIsInstance<ResultEntity>()
            participant.user?.
                let(mapperUser::asUserCensoredEntity)
        )
    }

    override fun asParticipant(entity: ParticipantEntity): Participant {
        return Participant(
            entity.participantId,
            entity.roomId,
            entity.userId,
            entity.currentPage,
            entity.timeLeft,
            entity.expired,
            entity.userId == currentUserId,
            entity.createdAt,
            entity.updatedAt, //, entity.results.filterIsInstance<Result>()
            entity.user?.
                let(mapperUser::asUserCensored)
        )
    }

    override fun asParticipant(response: Response<ParticipantEntity>): Participant {
        return when(response.data){
            is ParticipantEntity -> asParticipant(response.data)
            else -> throw IllegalStateException()
        }
    }

    override fun asPagingResource(
        onEvent: suspend (Int) -> List<Participant>): PagingSource<Int, Participant> =
        try { ResourcePaging(onEvent) } catch (e: Exception) {
            ResourcePaging { emptyList() }
        }


    override fun asParticipants(response: Response<List<ParticipantEntity>>): List<Participant> {
        return when(response.data){
            is List<*> -> {
                val list = response.data.filterIsInstance<ParticipantEntity>()

                list.map { asParticipant(it) }
            }
            else -> throw IllegalStateException()
        }
    }
}