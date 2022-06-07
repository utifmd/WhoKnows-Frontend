package com.dudegenuine.usecase.participant

import com.dudegenuine.model.Participation
import com.dudegenuine.model.Room
import com.dudegenuine.model.User
import com.dudegenuine.repository.contract.IParticipantRepository

/**
 * Thu, 02 Jun 2022
 * WhoKnows by utifmd
 **/
class GetParticipation constructor(
    private val repository: IParticipantRepository) {

    operator fun invoke(participantId: String, room: Room.Complete, currentUser: User.Complete):
            Participation = repository.participation(participantId, room, currentUser)
}