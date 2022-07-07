package com.dudegenuine.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dudegenuine.model.Impression

/**
 * Tue, 31 May 2022
 * WhoKnows by utifmd
 **/
@Entity(tableName = "rooms_censored")
data class RoomCensoredTable(
    @PrimaryKey
    @ColumnInfo(name = "roomId")
    val roomId: String,

    @ColumnInfo(name = "userId")
    val userId: String,

    @ColumnInfo(name = "minute")
    val minute: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "token")
    val token: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "expired")
    val expired: Boolean,

    @ColumnInfo(name = "privation")
    val privation: Boolean,

    @ColumnInfo(name = "usernameOwner")
    val usernameOwner: String,

    @ColumnInfo(name = "fullNameOwner")
    val fullNameOwner: String,

    @ColumnInfo(name = "questionSize")
    val questionSize: Int,

    @ColumnInfo(name = "isOwner")
    val isOwner: Boolean,

    @ColumnInfo(name = "impressions")
    val impressions: List<Impression>,

    @ColumnInfo(name = "participantSize")
    val participantSize: Int
)
