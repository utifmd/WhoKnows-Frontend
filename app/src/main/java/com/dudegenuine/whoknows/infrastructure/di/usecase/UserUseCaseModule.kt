package com.dudegenuine.whoknows.infrastructure.di.usecase

import android.content.BroadcastReceiver
import android.content.SharedPreferences
import com.dudegenuine.repository.contract.IUserRepository
import com.dudegenuine.usecase.user.*
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUserUseCaseModule

/**
 * Fri, 07 Jan 2022
 * WhoKnows by utifmd
 **/
class UserUseCaseModule(
    private val repository: IUserRepository,

    override val signInUser:
        SignInUser = SignInUser(repository),

    override val postUser:
        PostUser = PostUser(repository),

    override val getUser:
        GetUser = GetUser(repository),

    override val patchUser:
        PatchUser = PatchUser(repository),

    override val deleteUser:
        DeleteUser = DeleteUser(repository),

    override val getUsers:
        GetUsers = GetUsers(repository),

    override val getUsersParticipation:
        GetUsersParticipation = GetUsersParticipation(repository),

    override val signOutUser:
        SignOutUser = SignOutUser(repository),

    override val currentUserId: () ->
        String = { repository.currentUserId() },

    override val onNetworkReceived: (onConnected: (String) -> Unit) ->
        BroadcastReceiver = repository.networkReceived,

    override val onChangeCurrentBadge: (Int) ->
        Unit = repository.onChangeCurrentBadge,

    override val currentBadge: () ->
        Int = repository.currentBadge,

    override val registerPrefsListener: (SharedPreferences.OnSharedPreferenceChangeListener) ->
        Unit = repository.registerPrefsListener,

    override val unregisterPrefsListener: (SharedPreferences.OnSharedPreferenceChangeListener) ->
        Unit = repository.unregisterPrefsListener): IUserUseCaseModule