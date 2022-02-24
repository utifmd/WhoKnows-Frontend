package com.dudegenuine.remote.mapper.contract

import androidx.paging.PagingSource
import com.dudegenuine.model.Participant
import com.dudegenuine.remote.entity.ParticipantEntity
import com.dudegenuine.remote.entity.Response

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IParticipantDataMapper {
    fun asEntity(participant: Participant): ParticipantEntity
    fun asParticipant(entity: ParticipantEntity): Participant
    fun asParticipant(response: Response<ParticipantEntity>): Participant
    fun asParticipants(response: Response<List<ParticipantEntity>>): List<Participant>

    fun asPagingResource(
        onEvent: suspend (Int) -> List<Participant>): PagingSource<Int, Participant>
}