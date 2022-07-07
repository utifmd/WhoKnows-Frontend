package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory
import com.dudegenuine.repository.contract.dependency.local.IWorkerManager
import com.dudegenuine.usecase.search.SearchUser
import com.dudegenuine.usecase.user.*

/**
 * Fri, 03 Dec 2021
 * WhoKnows by utifmd
 **/
interface IUserUseCaseModule {
    val signInUser: SignInUser
    val signOutUser: SignOutUser
    val postUser: PostUser
    val getUser: GetUser
    val patchUser: PatchUser
    val deleteUser: DeleteUser
    val getUsers: GetUsers
    val getUsersParticipation: GetUsersParticipation
    val searchUser: SearchUser
    val isUsernameUsed: IsUsernameUsed

    val preferences: IPrefsFactory
    val workManager: IWorkerManager
}