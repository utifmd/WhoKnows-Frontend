package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import android.content.BroadcastReceiver
import android.content.SharedPreferences
import com.dudegenuine.usecase.user.*

/**
 * Fri, 03 Dec 2021
 * WhoKnows by utifmd
 **/
interface IUserUseCaseModule {
    val currentUserId: () -> String

    val signInUser: SignInUser
    val signOutUser: SignOutUser
    val postUser: PostUser
    val getUser: GetUser
    val patchUser: PatchUser
    val deleteUser: DeleteUser
    val getUsers: GetUsers

    val onNetworkReceived: (onConnected: (String) -> Unit) -> BroadcastReceiver

    val onChangeCurrentBadge: (Int) -> Unit
    val currentBadge: () -> Int

    val registerPrefsListener: (SharedPreferences.OnSharedPreferenceChangeListener) -> Unit
    val unregisterPrefsListener: (SharedPreferences.OnSharedPreferenceChangeListener) -> Unit

}