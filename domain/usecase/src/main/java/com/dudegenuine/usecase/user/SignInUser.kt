package com.dudegenuine.usecase.user

import com.dudegenuine.model.Resource
import com.dudegenuine.model.User
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.repository.contract.IMessagingRepository
import com.dudegenuine.repository.contract.IUserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Sat, 04 Dec 2021
 * WhoKnows by utifmd
 **/
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class SignInUser
    @Inject constructor(
    private val repoUser: IUserRepository,
    private val repoMsg: IMessagingRepository) {
    private val TAG: String = javaClass.simpleName

    operator fun invoke(params: User.Signer): Flow<Resource<User.Complete>> = flow {
        try {
            repoUser.remoteSignInFlow(params)
                .flatMapConcat(repoUser::localSignInFlow)
                .onStart{ emit(Resource.Loading()) }
                .onEach{ emit(Resource.Success(it)) }
                .collect()
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