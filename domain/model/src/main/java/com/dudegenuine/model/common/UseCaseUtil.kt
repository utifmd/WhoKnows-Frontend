package com.dudegenuine.model.common

import com.dudegenuine.model.Resource
import com.dudegenuine.model.Room
import com.dudegenuine.model.common.validation.HttpFailureException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
object UseCaseUtil {
    suspend fun tryCatch(onSuccess: suspend () -> Unit): Flow<Resource<List<Room>>> = flow {
        try {
            onSuccess()
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