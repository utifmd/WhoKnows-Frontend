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
 * Mon, 14 Feb 2022
 * WhoKnows by utifmd
 **/
class CreateMessaging @Inject constructor(
    private val repository: IMessagingRepository) {

    operator fun invoke(messaging: Messaging.GroupCreator): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())

            val (notification_key, error) = repository.create(messaging)
            emit(Resource.Success(error ?: notification_key))

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