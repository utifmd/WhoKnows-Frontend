package com.dudegenuine.whoknows.infrastructure.di.usecase

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
        GetUsers = GetUsers(repository)

): IUserUseCaseModule