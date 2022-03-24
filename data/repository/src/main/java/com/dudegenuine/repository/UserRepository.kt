package com.dudegenuine.repository

import android.content.BroadcastReceiver
import android.content.SharedPreferences
import android.util.Log
import com.dudegenuine.local.api.IPreferenceManager
import com.dudegenuine.local.api.IPreferenceManager.Companion.CURRENT_NOTIFICATION_BADGE
import com.dudegenuine.local.api.IPreferenceManager.Companion.CURRENT_USER_ID
import com.dudegenuine.local.api.IReceiverFactory
import com.dudegenuine.local.entity.UserTable
import com.dudegenuine.local.service.contract.ICurrentUserDao
import com.dudegenuine.model.User
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.remote.mapper.contract.IUserDataMapper
import com.dudegenuine.remote.service.contract.IUserService
import com.dudegenuine.repository.contract.IUserRepository
import com.dudegenuine.repository.contract.IUserRepository.Companion.CURRENT_USER_NOT_FOUND
import javax.inject.Inject

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
class UserRepository
    @Inject constructor(
    private val service: IUserService,
    private val local: ICurrentUserDao,
    private val prefs: IPreferenceManager,
    private val mapper: IUserDataMapper,
    private val receiver: IReceiverFactory): IUserRepository {
    private val TAG = javaClass.simpleName

    override val currentUserId: () ->
        String = { prefs.readString(CURRENT_USER_ID) }

    override val currentBadge: () ->
        Int = { prefs.readInt(CURRENT_NOTIFICATION_BADGE) }

    override val onChangeCurrentBadge: (Int) ->
        Unit = { prefs.write(CURRENT_NOTIFICATION_BADGE, it) }

    override val registerPrefsListener: (
        SharedPreferences.OnSharedPreferenceChangeListener) -> Unit = prefs.register

    override val unregisterPrefsListener: (
        SharedPreferences.OnSharedPreferenceChangeListener) -> Unit = prefs.unregister

    override val networkReceived: (onConnected: (String) -> Unit) ->
        BroadcastReceiver = receiver.networkReceived

    override suspend fun create(user: User.Complete): User.Complete {
        val remoteUser = mapper.asUser(service.create(mapper.asEntity(user)))

        return remoteUser.also { save(mapper.asUserTable(it)) }
    }

    override suspend fun read(id: String): User.Complete {
        val remoteUser = mapper.asUser(service.read(id))

        if (id == currentUserId())
            replace(mapper.asUserTable(remoteUser))

        Log.d(TAG, "read: triggered")

        return remoteUser
    }

    override suspend fun update(id: String, user: User.Complete): User.Complete {
        val remoteUser = mapper.asUser(service.update(id, mapper.asEntity(user)))

        return remoteUser.also { replace(mapper.asUserTable(it)) }
    }

    override suspend fun delete(id: String) {
        service.delete(id)

        unload(id)
    }

    override suspend fun list(page: Int, size: Int): List<User.Complete> = mapper.asUsers(
        service.list(page, size)
    )

    override suspend fun signIn(params: Map<String, String>): User.Complete {
        val remoteUser = mapper.asUser(service.signIn(mapper.asLogin(params)))

        return remoteUser.also { save(mapper.asUserTable(it)) }
    }

    override suspend fun signOut(): String {
        val finalId = currentUserId()

        unload(finalId)

        return finalId
    }

    override suspend fun save(userTable: UserTable) =
        local.create(userTable).also {
            prefs.write(CURRENT_USER_ID, userTable.userId)
        }

    override suspend fun replace(userTable: UserTable) {
        local.update(userTable)

        Log.d(TAG, "replace: triggered")
    }

    override suspend fun load(userId: String?): User.Complete {
        val finalId = userId ?: currentUserId()
        val currentUser = local.read(finalId)

        if (currentUser != null)
            return mapper.asUser(currentUser)
        else
            throw HttpFailureException(CURRENT_USER_NOT_FOUND)
    }

    override suspend fun unload(userId: String) {
        val currentUser = local.read(userId) //?: throw HttpFailureException(NOT_FOUND)

        if (currentUser != null) local.delete(currentUser)
        else local.delete()

        prefs.write(CURRENT_USER_ID, "")
    }
}