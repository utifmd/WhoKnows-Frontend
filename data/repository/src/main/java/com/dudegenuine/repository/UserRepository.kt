package com.dudegenuine.repository

import com.dudegenuine.model.User
import com.dudegenuine.model.request.LoginRequest
import com.dudegenuine.remote.mapper.contract.IUserDataMapper
import com.dudegenuine.remote.service.contract.IUserService
import com.dudegenuine.repository.contract.IUserRepository
import javax.inject.Inject

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
class UserRepository
    @Inject constructor(

    private val service: IUserService,
    private val mapper: IUserDataMapper ): IUserRepository {

    override suspend fun create(user: User): User = mapper.asUser (
        service.create(mapper.asEntity(user))
    )

    override suspend fun read(id: String): User = mapper.asUser (
        service.read(id)
    )

    override suspend fun update(id: String, user: User): User = mapper.asUser(
        service.update(id, mapper.asEntity(user))
    )

    override suspend fun delete(id: String) {
        service.delete(id)
    }

    override suspend fun list(page: Int, size: Int): List<User> = mapper.asUsers(
        service.list(page, size)
    )

    override suspend fun signIn(loginRequest: LoginRequest): User {
        return mapper.asUser(
            service.signIn(loginRequest)
        )
    }
}