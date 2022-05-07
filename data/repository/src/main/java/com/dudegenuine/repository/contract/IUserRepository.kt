package com.dudegenuine.repository.contract

import androidx.paging.PagingSource
import com.dudegenuine.local.entity.UserTable
import com.dudegenuine.model.User

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
interface IUserRepository {
    suspend fun create(user: User.Complete): User.Complete
    suspend fun read(id: String): User.Complete
    suspend fun update(id: String, user: User.Complete): User.Complete
    suspend fun delete(id: String)
    suspend fun list(page: Int, size: Int): List<User.Complete>
    suspend fun listOrderByParticipant(page: Int, size: Int): List<User.Censored>
    suspend fun signIn(params: Map<String, String>): User.Complete
    suspend fun signIn(model: User.Complete): User.Complete
    suspend fun signOut(user: User.Complete): String

    suspend fun load(userId: String? = null): User.Complete
    suspend fun save(userTable: UserTable)
    suspend fun replace(userTable: UserTable)
    suspend fun unload(userId: String)

    fun page(batchSize: Int): PagingSource<Int, User.Censored>

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