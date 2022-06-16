package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.contract.IMessagingRepository
import com.dudegenuine.repository.contract.IRoomRepository
import com.dudegenuine.repository.contract.IUserRepository
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory
import com.dudegenuine.repository.contract.dependency.local.IWorkerManager
import com.dudegenuine.usecase.search.SearchUser
import com.dudegenuine.usecase.user.*
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUserUseCaseModule

/**
 * Fri, 07 Jan 2022
 * WhoKnows by utifmd
 **/
class UserUseCaseModule(
    private val userRepository: IUserRepository,
    private val roomRepository: IRoomRepository,
    private val messagingRepository: IMessagingRepository,

    override val signInUser:
        SignInUser = SignInUser(userRepository, messagingRepository),

    override val postUser:
        PostUser = PostUser(userRepository, messagingRepository),

    override val getUser:
        GetUser = GetUser(userRepository),

    override val patchUser:
        PatchUser = PatchUser(userRepository),

    override val deleteUser:
        DeleteUser = DeleteUser(userRepository),

    override val getUsers:
        GetUsers = GetUsers(userRepository),

    override val getUsersParticipation:
        GetUsersParticipation = GetUsersParticipation(userRepository),

    override val signOutUser: SignOutUser = SignOutUser(
        userRepository,
        roomRepository,
        messagingRepository
    ),
    override val searchUser: SearchUser = SearchUser(userRepository),
    override val preferences: IPrefsFactory = userRepository.preference,
    override val workManager: IWorkerManager = roomRepository.workManager,
): IUserUseCaseModule