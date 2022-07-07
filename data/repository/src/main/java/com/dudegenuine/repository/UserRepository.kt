package com.dudegenuine.repository

import android.util.Log
import androidx.paging.PagingSource
import com.dudegenuine.local.entity.UserTable
import com.dudegenuine.local.service.IUsersDao
import com.dudegenuine.model.ResourcePaging
import com.dudegenuine.model.Search
import com.dudegenuine.model.User
import com.dudegenuine.model.common.Utility.encrypt
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.remote.mapper.contract.IUserDataMapper
import com.dudegenuine.remote.service.contract.IUserService
import com.dudegenuine.repository.contract.IUserRepository
import com.dudegenuine.repository.contract.IUserRepository.Companion.CURRENT_USER_NOT_FOUND
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory
import com.dudegenuine.repository.contract.dependency.local.IReceiverFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
class UserRepository
    @Inject constructor(
    private val service: IUserService,
    private val local: IUsersDao,
    private val mapper: IUserDataMapper,
    override val receiver: IReceiverFactory,
    override val preference: IPrefsFactory): IUserRepository {
    private val TAG = javaClass.simpleName

    override suspend fun remoteCreate(user: User.Complete): User.Complete {
        val remoteUser = mapper.asUser(
            service.create(mapper.asEntity(user.copy(password = encrypt(user.password))))
        )
        return remoteUser.also { localCreate(mapper.asUserTable(it)) }
    }
    override suspend fun remoteRead(id: String): User.Complete {
        /*if (id == preference.userId)
            localUpdate(mapper.asUserTable(remoteUser))
        Log.d(TAG, "read: triggered")*/
        return mapper.asUser(service.read(id))
    }
    override suspend fun remoteCount(username: String): Int {
        val response = service.count(username)
        return response.data ?: 0
    }
    override suspend fun remoteUpdate(id: String, user: User.Complete): User.Complete {
        val remoteUser = mapper.asUser(service.update(id, mapper.asEntity(user)))
        return remoteUser.also{ localUpdate(mapper.asUserTable(it)) }
    }
    override suspend fun remoteDelete(id: String) {
        service.delete(id)
        localDelete(id)
    }
    override suspend fun remoteList(page: Int, size: Int): List<User.Complete> = mapper.asUsers(
        service.list(page, size)
    )
    override suspend fun remoteListOrderByParticipant(page: Int, size: Int): List<User.Censored> = mapper.asUsersCensored(
        service.listOrderByParticipant(page, size)
    )
    override fun remotePages(batchSize: Int): PagingSource<Int, User.Censored> = mapper.asPagingSource { page ->
        remoteListOrderByParticipant(page, batchSize)
    }

    override fun remoteSearchPageCensored(
        query: String, batch: Int): PagingSource<Int, User.Censored> = try {
        ResourcePaging{ page -> mapper.asUsersCensored(
            service.listCensoredSearched(query, page, batch)) }
    } catch (e: Exception){ ResourcePaging{ emptyList() } }

    override fun remoteSearchSource(query: String, batch: Int): PagingSource<Int, Search<*>> = try {
        ResourcePaging{ page -> mapper.asUsersCensored(
            service.listCensoredSearched(query, page, batch)).map { Search.User(it) }
        }
    } catch (e: Exception){ ResourcePaging{ emptyList() } }

    override suspend fun remoteReadFlow(): Flow<User.Complete> =
        flowOf(remoteRead(preference.userId))

    override suspend fun remoteCreateFlow(user: User.Complete): Flow<User.Complete> =
        flowOf(remoteCreate(user))

    override suspend fun remoteUpdateFlow(user: User.Complete): Flow<User.Complete> =
        flowOf(remoteUpdate(user.id, user))

    override suspend fun remoteSignIn(params: User.Signer):
            User.Complete = mapper.asUser(service.signIn(params.copy(
        password = encrypt(params.password))))

    override suspend fun remoteSignInFlow(signer: User.Signer) =
        flowOf(remoteSignIn(signer))

    override suspend fun localSignIn(model: User.Complete): User.Complete {
        localCreate(mapper.asUserTable(model))
        onUserIdChange(model.id)
        return model
    }
    override suspend fun localSignInFlow(model: User.Complete): Flow<User.Complete> = flowOf(localSignIn(model))
    override suspend fun localSignOutFlow() = flowOf(localDelete(preference.userId))

    override suspend fun localCreate(userTable: UserTable) =
        local.create(userTable)

    override suspend fun localUpdate(userTable: UserTable) =
        local.update(userTable)

    override suspend fun localUpdate(user: User.Complete) {
        local.update(mapper.asUserTable(user))
    }

    override suspend fun localRead(userId: String?): User.Complete {
        val finalId = userId ?: preference.userId
        val currentUser = local.read(finalId)

        if (currentUser != null)
            return mapper.asUser(currentUser)
        else
            throw HttpFailureException(CURRENT_USER_NOT_FOUND)
    }

    override suspend fun localReadFlow(): Flow<User.Complete> =
        flowOf(localRead(preference.userId))

    override suspend fun localDelete(userId: String) {
        /*val currentUser = local.read(userId)

        if (currentUser != null) local.delete(currentUser)
        else */
        local.delete()
        onUserIdChange("")
        Log.d(TAG, "localDelete: triggered")
    }

    private fun onUserIdChange(fresh: String){
        preference.userId = fresh
    }
}