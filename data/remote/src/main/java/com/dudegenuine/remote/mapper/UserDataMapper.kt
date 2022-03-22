package com.dudegenuine.remote.mapper

import com.dudegenuine.local.entity.UserTable
import com.dudegenuine.model.*
import com.dudegenuine.model.User.Companion.PASSWORD
import com.dudegenuine.model.User.Companion.PAYLOAD
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.remote.entity.*
import com.dudegenuine.remote.mapper.contract.IUserDataMapper
import com.google.gson.Gson
import javax.inject.Inject

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
class UserDataMapper
    @Inject constructor(val gson: Gson): IUserDataMapper {

    override fun asEntity(user: User): UserEntity {
        return UserEntity(
            userId = user.id,
            fullName = user.fullName,
            username = user.username,
            phone = user.phone,
            email = user.email,
            password = user.password,
            profileUrl = user.profileUrl,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt,
            participants = user.participants
                .map(::asEntity),
            rooms = user.rooms
                .map(::asRoomCensoredEntity),
            notifications = user.notifications
                .map(::asNotifierEntity)
        )
    }

    override fun asUser(entity: UserEntity): User {
        return User(
            id = entity.userId,
            fullName = entity.fullName,
            username = entity.username,
            phone = entity.phone,
            email = entity.email,
            password = entity.password,
            profileUrl = entity.profileUrl,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            participants = entity.participants
                .map(::asParticipant),
            rooms = entity.rooms
                .map(::asRoomCensored),
            notifications = entity.notifications
                .map(::asNotification)
        )
    }

    override fun asUser(response: Response<UserEntity>): User {
        return when(response.data){
            is UserEntity -> asUser(response.data)
            else -> throw HttpFailureException(response.data.toString())
        }
    }

    override fun asUser(json: String): User {

        val adapter = gson.getAdapter(User::class.java)

        return adapter.fromJson(json)
    }

    override fun asUsers(response: Response<List<UserEntity>>): List<User> {
        val list = mutableListOf<User>()

        when(response.data){
            is List<*> -> response.data.filterIsInstance<UserEntity>().forEach {
                list.add(asUser(it))
            }
            else -> throw HttpFailureException(response.data.toString())
        }

        return list
    }

    override fun asLogin(params: Map<String, String>): UserEntity.LoginRequest {
        val payload = params[PAYLOAD] ?: throw HttpFailureException("incorrect payload")
        val password = params[PASSWORD] ?: throw HttpFailureException("incorrect password")

        return UserEntity.LoginRequest(
            payload, password
        )
    }

    override fun asUser(userTable: UserTable): User {
        return userTable.let { User(
            id = it.userId,
            fullName = it.fullName,
            email = it.email,
            phone = it.phone,
            username = it.username,
            password = it.password,
            profileUrl = it.profileUrl,
            createdAt = userTable.createdAt, //Date(it.createdAt),
            updatedAt = userTable.updatedAt, //it.updatedAt?.let { date -> Date(date) },
            participants = userTable.participants,
            rooms = userTable.rooms,
            notifications = userTable.notifications
        )}
    }

    override fun asUserTable(user: User): UserTable {
        return UserTable(
            userId = user.id,
            fullName = user.fullName,
            email = user.email,
            phone = user.phone,
            username = user.username,
            password = user.password,
            profileUrl = user.profileUrl,
            createdAt = user.createdAt,//.time,
            updatedAt = user.updatedAt,//?.time
            participants = user.participants,
            rooms = user.rooms,
            notifications = user.notifications
        )
    }

    override fun asEntity(participant: Participant): ParticipantEntity {
        return ParticipantEntity(
            participant.id,
            participant.roomId,
            participant.userId,
            participant.currentPage,
            participant.timeLeft,
            participant.expired,
            participant.createdAt,
            participant.updatedAt,
            participant.user?.
                let(::asUserCensoredEntity)
        )
    }

    override fun asParticipant(entity: ParticipantEntity): Participant {
        return Participant(
            entity.participantId,
            entity.roomId,
            entity.userId,
            entity.currentPage,
            entity.timeLeft,
            entity.expired,
            entity.createdAt,
            entity.updatedAt,
            entity.user?.
                let(::asUserCensored)
        )
    }

    override fun asNotifierEntity(notification: Notification): NotificationEntity {
        return NotificationEntity(
            notificationId = notification.notificationId,
            userId = notification.userId,
            roomId = notification.roomId,
            event = notification.event,
            seen = notification.seen,
            recipientId = notification.recipientId,
            createdAt = notification.createdAt,
            updatedAt = notification.updatedAt,
            sender = null,
        )
    }

    override fun asNotification(entity: NotificationEntity): Notification {
        return Notification(
            notificationId = entity.notificationId,
            userId = entity.userId,
            roomId = entity.roomId,
            event = entity.event,
            seen = entity.seen,
            recipientId = entity.recipientId,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            sender = null,
        )
    }

    override fun asUserCensoredEntity(user: UserCensored): UserCensoredEntity {
        return UserCensoredEntity(
            userId = user.userId,
            fullName = user.fullName,
            username = user.username,
            profileUrl = user.profileUrl
        )
    }

    override fun asUserCensored(entity: UserCensoredEntity): UserCensored {
        return UserCensored(
            userId = entity.userId,
            fullName = entity.fullName,
            username = entity.username,
            profileUrl = entity.profileUrl
        )
    }

    override fun asRoomCensoredEntity(room: RoomCensored): RoomCensoredEntity {
        return RoomCensoredEntity(
            roomId = room.roomId,
            userId = room.userId,
            minute = room.minute,
            title = room.title,
            description = room.description,
            expired = room.expired,
        )
    }

    override fun asRoomCensored(entity: RoomCensoredEntity): RoomCensored {
        return RoomCensored(
            roomId = entity.roomId,
            userId = entity.userId,
            minute = entity.minute,
            title = entity.title,
            description = entity.description,
            expired = entity.expired,
        )
    }
}