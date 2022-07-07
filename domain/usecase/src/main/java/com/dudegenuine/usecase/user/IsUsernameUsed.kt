package com.dudegenuine.usecase.user

import com.dudegenuine.model.Resource
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.repository.contract.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Tue, 28 Jun 2022
 * WhoKnows by utifmd
 **/
class IsUsernameUsed
    @Inject constructor(
    private val repository: IUserRepository) {

    operator fun invoke(username: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val count = repository.remoteCount(username)
            emit(Resource.Success(count > 0))
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