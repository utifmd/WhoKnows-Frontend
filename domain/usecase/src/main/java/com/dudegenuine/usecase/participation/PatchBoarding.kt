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
 * Sat, 19 Feb 2022
 * WhoKnows by utifmd
 **/
class PatchBoarding
    @Inject constructor(
    private val repository: IRoomRepository) {

    operator fun invoke(boarding: Participation): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())

            repository.updateBoardingLocal(boarding)
            emit(Resource.Success(boarding.participantId))

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