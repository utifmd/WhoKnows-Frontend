package com.dudegenuine.usecase.notification

import android.util.Log
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
class PostNotification
    @Inject constructor(
    private val reposNotification: INotificationRepository) {
    operator fun invoke(model: Notification): Flow<Resource<Notification>> = flow {
        try {
            emit(Resource.Loading())

            val notification = reposNotification.create(model)
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
    operator fun plus(model: Notification): Flow<Resource<Notification>> = flow {
        try {
            emit(Resource.Loading())
            Log.d("invoke", "${model.userId} ${model.roomId}")
            val notification = reposNotification.create(model)
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
    /*operator fun invoke(model: Notification, pusher: Messaging.Pusher): Flow<Resource<Notification>> = flow {
        try {
            emit(Resource.Loading())
            val messaging = Messaging.Pusher(model.title, model.event, EMPTY_STRING, to = model.to)
            val notification = reposNotification.create(model)
            reposMessaging.push(messaging)
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
    }*/
}