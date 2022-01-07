package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.usecase.user.*

/**
 * Fri, 03 Dec 2021
 * WhoKnows by utifmd
 **/
interface IUserUseCaseModule {
    fun postUser(): PostUser
    fun getUser(): GetUser
    fun patchUser(): PatchUser
    fun deleteUser(): DeleteUser
    fun getUsers(): GetUsers
    val signInUser: SignInUser
}