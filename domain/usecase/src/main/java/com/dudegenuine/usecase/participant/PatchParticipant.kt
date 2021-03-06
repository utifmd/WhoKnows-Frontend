package com.dudegenuine.usecase.participant

import com.dudegenuine.model.Participant
import com.dudegenuine.model.Resource
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.repository.contract.IParticipantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
class PatchParticipant
    @Inject constructor(
        private val  repository: IParticipantRepository) {
        operator fun invoke(id: String, current: Participant): Flow<Resource<Participant>> = flow {
            try {
                emit(Resource.Loading())
                val participant = repository.update(id, current)
                emit(Resource.Success(participant))
            } catch (e: HttpFailureException){
                emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_FAILURE_EXCEPTION))
            } catch (e: HttpException){
                emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_EXCEPTION))
            } catch (e: IOException){
                emit(Resource.Error(Resource.IO_EXCEPTION))
            } catch (e: Exception){
                emit(Resource.Error(e.localizedMessage ?: Resource.THROWABLE_EXCEPTION))
            }
        }
}