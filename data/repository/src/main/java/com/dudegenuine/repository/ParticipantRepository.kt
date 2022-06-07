package com.dudegenuine.repository

import androidx.paging.PagingSource
import com.dudegenuine.model.*
import com.dudegenuine.remote.mapper.contract.IParticipantDataMapper
import com.dudegenuine.remote.mapper.contract.IUserDataMapper
import com.dudegenuine.remote.service.contract.IParticipantService
import com.dudegenuine.repository.contract.IParticipantRepository
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory
import javax.inject.Inject

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
class ParticipantRepository
    @Inject constructor(
        private val service: IParticipantService,
        private val mapperUser: IUserDataMapper,
        private val mapperPpn: IParticipantDataMapper,
        override val prefs: IPrefsFactory): IParticipantRepository {

    override fun participationPage(
        room: Room.Complete, participantId: String): List<ParticipationPage> {
        val questionsLength = room.questions.size

        return room.questions.mapIndexed { index, quiz ->
            mapperPpn.asParticipationPage(index, questionsLength, quiz)
        }
    }

    override fun participation(
        participantId: String, room: Room.Complete, currentUser: User.Complete): Participation {
        val pages = participationPage(room, participantId)
        val participant = mapperUser.asUserCensored(currentUser)

        return mapperPpn.asAnParticipation(participantId, room, pages, participant)
    }

    override suspend fun create(participant: Participant): Participant = mapperPpn.asParticipant(
        service.create(mapperPpn.asEntity(participant)))

    override suspend fun read(id: String): Participant = mapperPpn.asParticipant(
        service.read(id))

    override suspend fun update(id: String, participant: Participant): Participant = mapperPpn.asParticipant(
        service.update(id, mapperPpn.asEntity(participant)))

    override suspend fun delete(id: String) =
        service.delete(id)

    override suspend fun list(page: Int, size: Int): List<Participant> = mapperPpn.asParticipants(
        service.list(page, size))

    override fun page(batchSize: Int): PagingSource<Int, Participant> =
        mapperPpn.asPagingResource { page ->
            list(page, batchSize)
        }


    override fun save(participant: Participant) {
        TODO("Not yet implemented")
    }
}