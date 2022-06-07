package com.dudegenuine.repository.contract

import androidx.paging.PagingSource
import com.dudegenuine.local.entity.UserTable
import com.dudegenuine.model.User
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory
import com.dudegenuine.repository.contract.dependency.local.IReceiverFactory
import kotlinx.coroutines.flow.Flow

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
interface IUserRepository {
    suspend fun remoteCreate(user: User.Complete): User.Complete
    suspend fun remoteRead(id: String): User.Complete
    suspend fun remoteUpdate(id: String, user: User.Complete): User.Complete
    suspend fun remoteDelete(id: String)
    suspend fun remoteList(page: Int, size: Int): List<User.Complete>
    suspend fun remoteListOrderByParticipant(page: Int, size: Int): List<User.Censored>
    suspend fun remoteSignIn(params: User.Signer): User.Complete

    fun remotePages(batchSize: Int): PagingSource<Int, User.Censored>
    //suspend fun onSignOut(user: User.Complete): String

    suspend fun signInFlow(signer: User.Signer): Flow<User.Complete>
    suspend fun remoteCreateFlow(user: User.Complete): Flow<User.Complete>
    suspend fun remoteUpdateFlow(user: User.Complete): Flow<User.Complete>
    suspend fun remoteReadFlow(): Flow<User.Complete>
    suspend fun clearCurrentUser(): Flow<Unit>

    suspend fun localSignIn(model: User.Complete): User.Complete
    suspend fun localRead(userId: String? = null): User.Complete
    suspend fun localCreate(userTable: UserTable)
    suspend fun localUpdate(userTable: UserTable)
    suspend fun localDelete(userId: String)



    val receiver: IReceiverFactory
    val preference: IPrefsFactory

    /*val currentUserId: () -> String
    val networkReceived: (onConnected: (String) -> Unit) -> BroadcastReceiver

    val onChangeCurrentBadge: (Int) -> Unit
    val currentBadge: () -> Int

    val registerPrefsListener: (SharedPreferences.OnSharedPreferenceChangeListener) -> Unit
    val unregisterPrefsListener: (SharedPreferences.OnSharedPreferenceChangeListener) -> Unit*/

    companion object {
        const val NOT_FOUND = "User not found."
        const val CURRENT_USER_NOT_FOUND = "Current user not found, please sign in first."
    }
}