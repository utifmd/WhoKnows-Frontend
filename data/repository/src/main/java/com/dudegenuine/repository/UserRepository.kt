package com.dudegenuine.repository

import com.dudegenuine.model.User
import com.dudegenuine.model.request.LoginRequest
import com.dudegenuine.model.validation.HttpFailureException
import com.dudegenuine.remote.mapper.contract.IUserDataMapper
import com.dudegenuine.remote.service.contract.IUserService
import com.dudegenuine.repository.contract.IUserRepository
import com.dudegenuine.repository.contract.IUserRepository.Companion.NOT_FOUND
import javax.inject.Inject

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
class UserRepository
    @Inject constructor(

    private val service: IUserService,
    private val mapper: IUserDataMapper ): IUserRepository {
    // private val TAG: String = javaClass.simpleName

    override suspend fun create(user: User): User = mapper.asUser (
        service.create(mapper.asEntity(user))
    )

    override suspend fun read(id: String): User = try { mapper.asUser(
        service.read(id))
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun update(id: String, user: User): User = try { mapper.asUser(
        service.update(id, mapper.asEntity(user)))
    } catch (e: Exception){
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }

    override suspend fun delete(id: String) {
        try { service.delete(id) } catch (e: Exception){
            throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
        }
    }

    override suspend fun list(page: Int, size: Int): List<User> = mapper.asUsers(
        service.list(page, size)
    )

    override suspend fun signIn(loginRequest: LoginRequest): User = try {
        mapper.asUser(service.signIn(loginRequest))
    } catch (e: Exception) {
        throw HttpFailureException(e.localizedMessage ?: NOT_FOUND)
    }
}