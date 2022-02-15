package com.dudegenuine.usecase.messaging

import com.dudegenuine.model.Resource
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.repository.contract.IMessagingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

/**
 * Mon, 14 Feb 2022
 * WhoKnows by utifmd
 **/
class GetMessaging(
    private val repository: IMessagingRepository) {

    operator fun invoke(keyName: String): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())

            val model = repository.get(keyName).notification_key

            emit(Resource.Success(model))

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