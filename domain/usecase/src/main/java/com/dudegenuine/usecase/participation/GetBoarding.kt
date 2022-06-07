package com.dudegenuine.usecase.participation

import com.dudegenuine.model.Participation
import com.dudegenuine.model.Resource
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

    operator fun invoke(): Flow<Resource<Participation>> = flow {
        try {
            emit(Resource.Loading())

            val boarding = repository.readBoardingLocal()
            emit(Resource.Success(boarding))

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