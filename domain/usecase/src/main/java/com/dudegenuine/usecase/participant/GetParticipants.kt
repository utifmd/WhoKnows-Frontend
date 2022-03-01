package com.dudegenuine.usecase.participant

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
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
class GetParticipants
    @Inject constructor(
    private val repository: IParticipantRepository) {

    operator fun invoke(batchSize: Int): Flow<PagingData<Participant>> = Pager(
        PagingConfig(batchSize, maxSize = 200, enablePlaceholders = true))

    { repository.page(batchSize) }.flow


    operator fun invoke(page: Int, size: Int): Flow<Resource<List<Participant>>> = flow {
        try {
            emit(Resource.Loading())
            val list = repository.list(page, size)
            emit(Resource.Success(list))
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