package com.dudegenuine.remote.mapper

import android.util.Log
import androidx.paging.PagingSource
import com.dudegenuine.local.entity.UserTable
import com.dudegenuine.model.*
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
    @Inject constructor(
    private val gson: Gson,
    private val currentUserId: String): IUserDataMapper {
    private val TAG: String = javaClass.simpleName

    override fun asEntityCensored(user: User.Censored): UserEntity.Censored {
        return UserEntity.Censored(
            userId = user.userId,
            fullName = user.fullName,
            username = user.username,
            profileUrl = user.profileUrl,
            tokens = user.tokens
        )
    }

    override fun asUsersCensored(
        response: Response<List<UserEntity.Censored>>): List<User.Censored> {

        return when(response.data){
            is List<*> -> response.data
                .filterIsInstance<UserEntity.Censored>().map(::asUserCensored)
            else -> throw HttpFailureException(response.data.toString())
        }
    }

    @Throws(Exception::class)
    override fun asEntity(user: User.Complete): UserEntity.Complete {
        return UserEntity.Complete(
            userId = user.id,
            fullName = user.fullName,
            username = user.username,
            phone = user.phone,
            email = user.email,
            password = user.password,
            profileUrl = user.profileUrl,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt,
            tokens = user.tokens,
            participants = user.participants
                .map(::asEntity),
            rooms = user.rooms
                .map(::asRoomCensoredEntity),
            notifications = user.notifications
                .map(::asNotifierEntity)
        )
    }

    override fun asUser(entity: UserEntity.Complete): User.Complete {
        return User.Complete(
            id = entity.userId,
            fullName = entity.fullName,
            username = entity.username,
            phone = entity.phone,
            email = entity.email,
            password = entity.password,
            profileUrl = entity.profileUrl,
            isCurrentUser = entity.userId == currentUserId,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            tokens = entity.tokens,
            participants = entity.participants
                .map(::asParticipant),
            rooms = entity.rooms
                .map(::asRoomCensored),
            notifications = entity.notifications
                .map(::asNotification)
        )
    }

    override fun asUser(response: Response<UserEntity.Complete>): User.Complete {
        return when(response.data){
            is UserEntity.Complete -> asUser(response.data)
            else -> throw HttpFailureException(response.data.toString())
        }
    }

    override fun asUser(json: String): User.Complete {

        val adapter = gson.getAdapter(User.Complete::class.java)

        return adapter.fromJson(json)
    }

    override fun asUsers(response: Response<List<UserEntity.Complete>>): List<User.Complete> {
        val list = mutableListOf<User.Complete>()

        when(response.data){
            is List<*> -> response.data.filterIsInstance<UserEntity.Complete>().forEach {
                list.add(asUser(it))
            }
            else -> throw HttpFailureException(response.data.toString())
        }

        return list
    }

    override fun asPagingSource(
        onEvent: suspend (Int) -> List<User.Censored>): PagingSource<Int, User.Censored> =
        try { ResourcePaging(onEvent) } catch (e: Exception) {
            Log.d(TAG, "asPagingResource: ${e.localizedMessage}")
            ResourcePaging { emptyList() }
        }

    override fun asUser(userTable: UserTable): User.Complete {
        return userTable.let { User.Complete(
            id = it.userId,
            fullName = it.fullName,
            email = it.email,
            phone = it.phone,
            username = it.username,
            password = it.password,
            profileUrl = it.profileUrl,
            isCurrentUser = it.userId == currentUserId,
            createdAt = userTable.createdAt, //Date(it.createdAt),
            updatedAt = userTable.updatedAt, //it.updatedAt?.let { date -> Date(date) },
            participants = userTable.participants,
            tokens = it.tokens,
            rooms = userTable.rooms,
            notifications = userTable.notifications
        )}
    }

    override fun asUserTable(user: User.Complete): UserTable {
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
            tokens = user.tokens,
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
            entity.userId == currentUserId,
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

    override fun asUserCensoredEntity(user: User.Censored): UserEntity.Censored {
        return UserEntity.Censored(
            userId = user.userId,
            fullName = user.fullName,
            username = user.username,
            profileUrl = user.profileUrl,
            tokens =  user.tokens
        )
    }

    override fun asUserCensored(entity: UserEntity.Censored): User.Censored {
        return User.Censored(
            userId = entity.userId,
            fullName = entity.fullName,
            username = entity.username,
            profileUrl = entity.profileUrl,
            isCurrentUser = entity.userId == currentUserId,
            tokens = entity.tokens
        )
    }

    override fun asUserCensored(user: User.Complete): User.Censored {
        return User.Censored(
            userId = user.id,
            fullName = user.fullName,
            username = user.username,
            profileUrl = user.profileUrl,
            isCurrentUser = user.id == currentUserId,
            tokens = user.tokens
        )
    }

    override fun asRoomCensoredEntity(room: Room.Censored): RoomEntity.Censored {
        return RoomEntity.Censored(
            roomId = room.roomId,
            userId = room.userId,
            minute = room.minute,
            title = room.title,
            token = room.token,
            description = room.description,
            expired = room.expired,
            private = room.private,
            usernameOwner = room.usernameOwner,
            fullNameOwner = room.fullNameOwner,
            questionSize = room.questionSize,
            participantSize = room.participantSize,
            impressions = emptyList()
        )
    }

    override fun asRoomCensored(entity: RoomEntity.Censored): Room.Censored {
        return Room.Censored(
            roomId = entity.roomId,
            userId = entity.userId,
            minute = entity.minute,
            title = entity.title,
            token = entity.token,
            description = entity.description,
            expired = entity.expired,
            usernameOwner = entity.usernameOwner,
            fullNameOwner = entity.fullNameOwner,
            questionSize = entity.questionSize,
            participantSize = entity.participantSize,
            isOwner = entity.userId == currentUserId,
            private = entity.private ?: false,
            impressed = entity.impressions.any{ it.userId == currentUserId },
            impressionSize = entity.impressions.size
        )
    }
}