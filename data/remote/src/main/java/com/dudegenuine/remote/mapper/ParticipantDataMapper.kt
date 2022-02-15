package com.dudegenuine.remote.mapper

import com.dudegenuine.model.Participant
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
    private val mapperUser: IUserDataMapper): IParticipantDataMapper {

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