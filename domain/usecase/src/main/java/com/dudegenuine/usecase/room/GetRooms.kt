package com.dudegenuine.usecase.room

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
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
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
class GetRooms
    @Inject constructor(
    private val repository: IRoomRepository) {

    operator fun invoke(size: Int): Flow<PagingData<Room.Complete>> {
        val config = PagingConfig(size,
            enablePlaceholders = true, maxSize = 200)

        val pager = Pager(config) { repository.page(size) }

        return pager.flow
    }

    operator fun invoke(page: Int, size: Int): Flow<Resource<List<Room.Complete>>> = flow {
        try {
            emit(Resource.Loading())
            val rooms = repository.list(page, size)

            emit(Resource.Success(rooms))
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

    operator fun invoke(userId: String, size: Int): Flow<PagingData<Room.Complete>> {
        val config = PagingConfig(size,
            enablePlaceholders = true, maxSize = 200)

        val pager = Pager(config) { repository.page(userId, size) }

        return pager.flow
    }
}