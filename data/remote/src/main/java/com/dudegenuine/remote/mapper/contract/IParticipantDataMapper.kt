package com.dudegenuine.remote.mapper.contract

import com.dudegenuine.model.Participant
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.entity.ParticipantEntity

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface IParticipantDataMapper {
    fun asEntity(participant: Participant): ParticipantEntity
    fun asParticipant(entity: ParticipantEntity): Participant
    fun asParticipant(response: Response<ParticipantEntity>): Participant
    fun asParticipants(response: Response<List<ParticipantEntity>>): List<Participant>
}