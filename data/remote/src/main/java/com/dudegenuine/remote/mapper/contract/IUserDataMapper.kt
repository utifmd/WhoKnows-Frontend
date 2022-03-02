package com.dudegenuine.remote.mapper.contract

import com.dudegenuine.local.entity.UserTable
import com.dudegenuine.model.Participant
import com.dudegenuine.model.RoomCensored
import com.dudegenuine.model.User
import com.dudegenuine.model.UserCensored
import com.dudegenuine.remote.entity.*

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
interface IUserDataMapper {
    fun asEntity(user: User): UserEntity
    fun asUser(entity: UserEntity): User
    fun asUser(response: Response<UserEntity>): User
    fun asUser(json: String): User
    fun asUsers(response: Response<List<UserEntity>>): List<User>
    fun asLogin(params: Map<String, String>): UserEntity.LoginRequest

    /*fun asUserOrNull(currentUser: CurrentUser?): User?*/
    fun asUserTable(user: User): UserTable
    fun asUser(userTable: UserTable): User

    fun asEntity(participant: Participant): ParticipantEntity
    fun asParticipant(entity: ParticipantEntity): Participant

    fun asUserCensoredEntity(user: UserCensored): UserCensoredEntity
    fun asUserCensored(entity: UserCensoredEntity): UserCensored

    fun asRoomCensoredEntity(room: RoomCensored): RoomCensoredEntity
    fun asRoomCensored(entity: RoomCensoredEntity): RoomCensored
}