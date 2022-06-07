package com.dudegenuine.remote.mapper.contract

import androidx.paging.PagingSource
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
    fun asEntity(user: User.Complete): UserEntity.Complete
    fun asUser(entity: UserEntity.Complete): User.Complete
    fun asUser(response: Response<UserEntity.Complete>): User.Complete
    fun asUser(json: String): User.Complete
    fun asUsers(response: Response<List<UserEntity.Complete>>): List<User.Complete>
    fun asEntityCensored(user: User.Censored): UserEntity.Censored
    //fun asUserCensored(entity: UserEntity.Complete): User.Censored
    //fun asUserCensored(response: Response<UserEntity.Complete>): User.Censored
    //fun asUserCensored(json: String): User.Censored
    fun asUsersCensored(response: Response<List<UserEntity.Censored>>): List<User.Censored>
//    fun asLogin(params: Map<String, String>): User.Signer

    /*fun asUserOrNull(currentUser: CurrentUser?): User?*/
    fun asUserTable(user: User.Complete): UserTable
    fun asUser(userTable: UserTable): User.Complete

    fun asEntity(participant: Participant): ParticipantEntity
    fun asParticipant(entity: ParticipantEntity): Participant

    fun asNotifierEntity(notification: Notification): NotificationEntity
    fun asNotification(entity: NotificationEntity): Notification

    fun asUserCensoredEntity(user: User.Censored): UserEntity.Censored
    fun asUserCensored(entity: UserEntity.Censored): User.Censored
    fun asUserCensored(user: User.Complete): User.Censored

    fun asRoomCensoredEntity(room: Room.Censored): RoomEntity.Censored
    fun asRoomCensored(entity: RoomEntity.Censored): Room.Censored

    fun asPagingSource(onEvent: suspend (Int) -> List<User.Censored>): PagingSource<Int, User.Censored>
}