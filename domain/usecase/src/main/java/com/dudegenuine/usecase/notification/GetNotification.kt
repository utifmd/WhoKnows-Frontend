package com.dudegenuine.usecase.notification

import com.dudegenuine.model.Notification
import com.dudegenuine.model.Resource
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.repository.contract.INotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
class GetNotification
    @Inject constructor(
    private val repository: INotificationRepository) {

    operator fun invoke(id: String):  Flow<Resource<Notification>> = flow {
        try {
            emit(Resource.Loading())

            val notification = repository.read(id)
            emit(Resource.Success(notification))

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