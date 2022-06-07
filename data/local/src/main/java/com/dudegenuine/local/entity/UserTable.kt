package com.dudegenuine.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dudegenuine.model.Notification
import com.dudegenuine.model.Participant
import com.dudegenuine.model.Room
import java.util.*

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
@Entity(tableName = "users")
data class UserTable(
    @PrimaryKey
    @ColumnInfo(name = "userId")
    val userId: String,

    @ColumnInfo(name = "fullName")
    val fullName: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "phone")
    val phone: String,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "profileUrl")
    val profileUrl: String,

    @ColumnInfo(name = "createdAt")
    val createdAt: Date,

    @ColumnInfo(name = "updatedAt")
    val updatedAt: Date?,

    @ColumnInfo(name = "tokens")
    val tokens: List<String>,

    @ColumnInfo(name = "participants")
    val participants: List<Participant>,

    @ColumnInfo(name = "rooms")
    val rooms: List<Room.Censored>,

    @ColumnInfo(name = "notifications")
    val notifications: List<Notification>,
)
