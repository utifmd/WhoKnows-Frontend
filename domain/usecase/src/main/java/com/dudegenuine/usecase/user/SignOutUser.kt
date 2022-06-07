package com.dudegenuine.usecase.user

import com.dudegenuine.model.Resource
import com.dudegenuine.model.common.Utility.concatenate
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.repository.contract.IMessagingRepository
import com.dudegenuine.repository.contract.IRoomRepository
import com.dudegenuine.repository.contract.IUserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Tue, 18 Jan 2022
 * WhoKnows by utifmd
 **/
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class SignOutUser
    @Inject constructor(
    private val repoUser: IUserRepository,
    private val repoRoom: IRoomRepository,
    private val repoMsg: IMessagingRepository) {
    private val preferences get() = repoUser.preference

    operator fun invoke(): Flow<Resource<String>> = flow {
        try {
            repoUser.remoteReadFlow().flatMapConcat{ currentUser ->
                val joins = currentUser.participants.map { it.roomId }
                val owns = currentUser.rooms.map { it.roomId }
                val tokens = currentUser.tokens.filter { it != preferences.tokenId }
                val latestUser = currentUser.copy(tokens = tokens)

                flowOf(repoUser.remoteUpdateFlow(latestUser)
                    .flatMapMerge{ concatenate(joins, owns).asFlow()
                        .flatMapConcat(repoMsg::unregisterGroupTokenFlow) })
                    .flatMapMerge{ repoRoom.clearParticipation() }
                    .flatMapMerge{ repoUser.clearCurrentUser() }
                    .onStart{ emit(Resource.Loading()) }
                    .onCompletion{ if (it == null)
                        emit(Resource.Success("Signed out successfully")) else
                        emit(Resource.Error(it.localizedMessage ?: "Error occurred"))
                    }
            }.collect()
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