package com.dudegenuine.usecase.user

import com.dudegenuine.model.Resource
import com.dudegenuine.model.User
import com.dudegenuine.repository.contract.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
class GetUser( // @Inject
    private val repository: IUserRepository) {
    operator fun invoke(id: String): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())

            val user = repository.read(id)
            emit(Resource.Success(user))

        } catch (e: HttpException){
            emit(Resource.Error(
                message = e.localizedMessage ?: "An expected error occurred"))
        } catch (e: IOException){
            emit(Resource.Error(
                message = "Check your internet connection."))
        }
    }
}