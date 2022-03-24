package com.dudegenuine.remote.mapper.contract

import com.dudegenuine.local.entity.UserTable
import com.dudegenuine.model.Notification
import com.dudegenuine.model.Participant
import com.dudegenuine.model.Room
import com.dudegenuine.model.User
import com.dudegenuine.remote.entity.*

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
interface IUserDataMapper {
    fun asEntity(user: User.Complete): UserEntity
    fun asUser(entity: UserEntity): User.Complete
    fun asUser(response: Response<UserEntity>): User.Complete
    fun asUser(json: String): User.Complete
    fun asUsers(response: Response<List<UserEntity>>): List<User.Complete>
    fun asLogin(params: Map<String, String>): UserEntity.LoginRequest

    /*fun asUserOrNull(currentUser: CurrentUser?): User?*/
    fun asUserTable(user: User.Complete): UserTable
    fun asUser(userTable: UserTable): User.Complete

    fun asEntity(participant: Participant): ParticipantEntity
    fun asParticipant(entity: ParticipantEntity): Participant

    fun asNotifierEntity(notification: Notification): NotificationEntity
    fun asNotification(entity: NotificationEntity): Notification

    fun asUserCensoredEntity(user: User.Censored): UserCensoredEntity
    fun asUserCensored(entity: UserCensoredEntity): User.Censored

    fun asRoomCensoredEntity(room: Room.Censored): RoomCensoredEntity
    fun asRoomCensored(entity: RoomCensoredEntity): Room.Censored
}