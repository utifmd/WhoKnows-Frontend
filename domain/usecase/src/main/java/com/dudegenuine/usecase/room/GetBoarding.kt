package com.dudegenuine.usecase.room

import com.dudegenuine.model.Resource
import com.dudegenuine.model.Room
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.repository.contract.IRoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Fri, 18 Feb 2022
 * WhoKnows by utifmd
 **/
class GetBoarding
    @Inject constructor(
    private val repository: IRoomRepository) {

    operator fun invoke(): Flow<Resource<Room.RoomState.BoardingQuiz>> = flow {
        try {
            emit(Resource.Loading())

            val boarding = repository.load()
            emit(Resource.Success(boarding))

        } catch (e: HttpFailureException){
            emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_FAILURE_EXCEPTION))
        } catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_EXCEPTION))
        } catch (e: IOException){
            emit(Resource.Error(Resource.IO_EXCEPTION))
        } catch (e: Exception){
            emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_EXCEPTION))
        }
    }
}