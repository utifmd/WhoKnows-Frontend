package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.contract.IUserRepository
import com.dudegenuine.usecase.user.*
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUserUseCaseModule

/**
 * Fri, 07 Jan 2022
 * WhoKnows by utifmd
 **/
class UserUseCase(
    val repository: IUserRepository,

    override val signInUser: SignInUser =
        SignInUser(repository)): IUserUseCaseModule {

    override fun postUser(): PostUser = PostUser(repository)

    override fun getUser(): GetUser = GetUser(repository)

    override fun patchUser(): PatchUser = PatchUser(repository)

    override fun deleteUser(): DeleteUser = DeleteUser(repository)

    override fun getUsers(): GetUsers = GetUsers(repository)

    //override fun signInUser(): SignInUser = SignInUser(repository)
}