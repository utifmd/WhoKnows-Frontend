package com.dudegenuine.repository

import android.util.Log
import com.dudegenuine.local.api.IPreferenceManager
import com.dudegenuine.local.api.IPreferenceManager.Companion.CURRENT_USER_ID
import com.dudegenuine.local.entity.CurrentUser
import com.dudegenuine.local.service.contract.ICurrentUserDao
import com.dudegenuine.model.User
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.remote.mapper.contract.IUserDataMapper
import com.dudegenuine.remote.service.contract.IUserService
import com.dudegenuine.repository.contract.IUserRepository
import com.dudegenuine.repository.contract.IUserRepository.Companion.CURRENT_USER_NOT_FOUND
import com.dudegenuine.repository.contract.IUserRepository.Companion.NOT_FOUND
import javax.inject.Inject

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
class UserRepository
    @Inject constructor(
    private val service: IUserService,
    private val dao: ICurrentUserDao,
    private val prefs: IPreferenceManager,
    private val mapper: IUserDataMapper): IUserRepository {

    private val TAG = javaClass.simpleName

    override val currentUserId: () -> String = {
        prefs.read(CURRENT_USER_ID)
    }

    override suspend fun create(user: User): User = try {
        val remoteUser = mapper.asUser(service.create(mapper.asEntity(user)))

        remoteUser.also { save(mapper.asCurrentUser(it)) }
    } catch (e: Exception){

        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun read(id: String): User = try {
        val remoteUser = mapper.asUser(service.read(id)) /*val localUser = load(id)*/

        remoteUser
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun update(id: String, user: User): User = try {
        val remoteUser = mapper.asUser(service.update(id, mapper.asEntity(user)))

        remoteUser.also { replace(mapper.asCurrentUser(it)) }
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun delete(id: String) { try {
        service.delete(id)

        unload(id)
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }}

    override suspend fun list(page: Int, size: Int): List<User> = mapper.asUsers(
        service.list(page, size)
    )

    override suspend fun signIn(params: Map<String, String>): User = try {
        val remoteUser = mapper.asUser(service.signIn(mapper.asLogin(params)))

        remoteUser.also { save(mapper.asCurrentUser(it)) }
    } catch (e: Exception) {

        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun signOut(): String {
        val finalId = currentUserId()

        unload(finalId)

        return finalId
    }

    override suspend fun save(currentUser: CurrentUser) =
        dao.create(currentUser).also {
            prefs.write(CURRENT_USER_ID, currentUser.userId)
        }

    override suspend fun replace(currentUser: CurrentUser) {
        dao.update(currentUser)

        Log.d(TAG, "replace: triggered")
    }

    override suspend fun load(userId: String?): User {
        val finalId = userId ?: currentUserId()
        val currentUser = dao.read(finalId)

        if (currentUser != null)
            return mapper.asUser(currentUser)
        else
            throw HttpFailureException(CURRENT_USER_NOT_FOUND)
    }

    override suspend fun unload(userId: String) {
        val currentUser = dao.read(userId) //?: throw HttpFailureException(NOT_FOUND)

        currentUser?.let {
            prefs.write(CURRENT_USER_ID, "")
            dao.delete(it)
        }
    }
}