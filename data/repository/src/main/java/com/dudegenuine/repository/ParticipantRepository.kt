package com.dudegenuine.repository

import androidx.paging.PagingSource
import com.dudegenuine.model.Participant
import com.dudegenuine.remote.mapper.contract.IParticipantDataMapper
import com.dudegenuine.remote.service.contract.IParticipantService
import com.dudegenuine.repository.contract.IParticipantRepository
import javax.inject.Inject

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
class ParticipantRepository
    @Inject constructor(
    private val service: IParticipantService,
    private val mapper: IParticipantDataMapper
    ): IParticipantRepository {

    override suspend fun create(participant: Participant): Participant = mapper.asParticipant(
        service.create(mapper.asEntity(participant))
    )

    override suspend fun read(id: String): Participant = mapper.asParticipant(
        service.read(id))

    override suspend fun update(id: String, participant: Participant): Participant = mapper.asParticipant(
        service.update(id, mapper.asEntity(participant)))

    override suspend fun delete(id: String) =
        service.delete(id)

    override suspend fun list(page: Int, size: Int): List<Participant> = mapper.asParticipants(
        service.list(page, size))

    override fun page(batchSize: Int): PagingSource<Int, Participant> =
        mapper.asPagingResource { page ->
            list(page, batchSize)
        }


    override fun save(participant: Participant) {
        TODO("Not yet implemented")
    }
}