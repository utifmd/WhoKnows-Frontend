package com.dudegenuine.repository

import com.dudegenuine.model.Participant
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.remote.mapper.contract.IParticipantDataMapper
import com.dudegenuine.remote.service.contract.IParticipantService
import com.dudegenuine.repository.contract.IParticipantRepository
import com.dudegenuine.repository.contract.IParticipantRepository.Companion.NOT_FOUND
import javax.inject.Inject

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
class ParticipantRepository
    @Inject constructor(
    private val service: IParticipantService,
    private val mapper: IParticipantDataMapper): IParticipantRepository {

    override suspend fun create(participant: Participant): Participant = try { mapper.asParticipant(
        service.create(mapper.asEntity(participant)))
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun read(id: String): Participant = try { mapper.asParticipant(
        service.read(id))
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun update(id: String, participant: Participant): Participant = try { mapper.asParticipant(
        service.update(id, mapper.asEntity(participant)))
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun delete(id: String) = try {
        service.delete(id)
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun list(page: Int, size: Int): List<Participant> = try { mapper.asParticipants(
        service.list(page, size))
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }
}