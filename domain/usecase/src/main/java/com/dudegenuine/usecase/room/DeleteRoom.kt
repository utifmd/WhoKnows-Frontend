package com.dudegenuine.usecase.room

import com.dudegenuine.model.Resource
import com.dudegenuine.model.validation.HttpFailureException
import com.dudegenuine.repository.contract.IRoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
class DeleteRoom
    @Inject constructor(private val repository: IRoomRepository) {
    operator fun invoke(id: String): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            repository.delete(id)
            emit(Resource.Success(id))
        } catch (e: HttpFailureException){
            emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_FAILURE_EXCEPTION))
        } catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_EXCEPTION))
        } catch (e: IOException){
            emit(Resource.Error(Resource.IO_EXCEPTION))
        }
    }
}