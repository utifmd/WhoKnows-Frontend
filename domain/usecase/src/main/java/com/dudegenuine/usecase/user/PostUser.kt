package com.dudegenuine.usecase.user

import com.dudegenuine.model.Resource
import com.dudegenuine.model.User
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.repository.contract.IUserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Sat, 04 Dec 2021
 * WhoKnows by utifmd
 **/
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class PostUser
    @Inject constructor(
    private val repository: IUserRepository) {
    private val tokenId get() = repository.preference.tokenId

    operator fun invoke(user: User.Complete): Flow<Resource<User.Complete>> = flow {
        try {
            emit(Resource.Loading())
            val remoteUser = repository.remoteCreate(user.copy(tokens = listOf(tokenId)))
            val localUser = repository.localSignIn(remoteUser)
            Resource.Success(localUser)
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