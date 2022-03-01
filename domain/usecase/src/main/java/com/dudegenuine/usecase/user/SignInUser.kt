package com.dudegenuine.usecase.user

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
 * Sat, 04 Dec 2021
 * WhoKnows by utifmd
 **/
class SignInUser
    @Inject constructor(
        private val repository: IUserRepository) {
        operator fun invoke(params: Map<String, String>): Flow<Resource<User>> = flow {
            try {
                emit(Resource.Loading())
                val signedInUser = repository.signIn(params)
                emit(Resource.Success(signedInUser))

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