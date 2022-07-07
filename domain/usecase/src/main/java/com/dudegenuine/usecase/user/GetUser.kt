package com.dudegenuine.usecase.user

import android.util.Log
import com.dudegenuine.model.Resource
import com.dudegenuine.model.User
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.repository.contract.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
class GetUser
    @Inject constructor(
    private val repository: IUserRepository) {
    private val TAG: String = javaClass.simpleName

    operator fun invoke(id: String):
            Flow<Resource<User.Complete>> = flow {

        try {
            val localUser = repository.localRead()
            emit(Resource.Loading(localUser))

            val remoteUser = repository.remoteRead(id)
            emit(Resource.Success(remoteUser))

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

    operator fun invoke():
            Flow<Resource<User.Complete>> = flow {

        try {
            emit(Resource.Loading())
            val localUser = repository.localRead()

            Log.d(TAG, localUser.toString())

            emit(Resource.Success(localUser))

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