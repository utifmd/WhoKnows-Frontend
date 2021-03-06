package com.dudegenuine.usecase.messaging

import com.dudegenuine.model.Messaging
import com.dudegenuine.model.Resource
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.repository.contract.IMessagingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Tue, 22 Feb 2022
 * WhoKnows by utifmd
 **/
class RemoveMessaging @Inject constructor(
    private val repository: IMessagingRepository) {

    operator fun invoke(messaging: Messaging): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())

            val model = repository.remove(messaging).string()
            emit(Resource.Success(model))

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