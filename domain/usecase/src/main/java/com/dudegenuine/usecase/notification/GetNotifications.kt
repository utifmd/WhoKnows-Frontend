package com.dudegenuine.usecase.notification

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.dudegenuine.model.Notification
import com.dudegenuine.model.Resource
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.repository.contract.INotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
class GetNotifications
    @Inject constructor(
    private val repository: INotificationRepository) {

    operator fun invoke(recipientId: String, size: Int):
            Flow<PagingData<Notification>> = Pager(PagingConfig(size, enablePlaceholders = true, maxSize = 200)){
        repository.pages(recipientId, size)
    }.flow.map { data ->
        data.map { notify ->
            notify.copy(isDetail = !notify.event.contains("like"))
        }
    }

    operator fun invoke(page: Int, size: Int):
            Flow<Resource<List<Notification>>> = flow {

        try {
            emit(Resource.Loading())

            val notifications = repository.list(page, size)
            emit(Resource.Success(notifications))

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

    operator fun invoke(recipientId: String, page: Int, size: Int):
            Flow<Resource<List<Notification>>> = flow {

        try {
            emit(Resource.Loading())

            val notifications = repository.list(recipientId, page, size)
            emit(Resource.Success(notifications))

        } catch (e: HttpFailureException){
            emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_FAILURE_EXCEPTION))
        } catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_EXCEPTION))
        } catch (e: IOException){
            emit(Resource.Error(Resource.IO_EXCEPTION))
        } catch (e: IllegalStateException){
            emit(Resource.Error(e.localizedMessage ?: Resource.ILLEGAL_STATE_EXCEPTION))
        } catch (e: Exception){
            emit(Resource.Error(e.localizedMessage ?: Resource.THROWABLE_EXCEPTION))
        }
    }
}