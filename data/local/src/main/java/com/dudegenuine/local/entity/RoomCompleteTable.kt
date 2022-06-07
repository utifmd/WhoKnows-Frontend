package com.dudegenuine.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dudegenuine.model.Participant
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.User
import java.util.*

/**
 * Tue, 31 May 2022
 * WhoKnows by utifmd
 **/
@Entity(tableName = "rooms_complete")
data class RoomCompleteTable(
    @PrimaryKey
    @ColumnInfo(name = "roomId")
    val roomId: String,

    @ColumnInfo(name = "userId")
    val userId: String,

    @ColumnInfo(name = "minute")
    var minute: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "isOwner")
    val isOwner: Boolean,

    @ColumnInfo(name = "expired")
    var expired: Boolean,

    @ColumnInfo(name = "privation")
    val privation: Boolean,

    @ColumnInfo(name = "createdAt")
    var createdAt: Date,

    @ColumnInfo(name = "updatedAt")
    var updatedAt: Date?,

    @ColumnInfo(name = "impressionSize")
    val impressionSize: Int,

    @ColumnInfo(name = "impressed")
    val impressed: Boolean,

    @ColumnInfo(name = "user")
    val user: User.Censored?,

    @ColumnInfo(name = "questions")
    val questions: List<Quiz.Complete>,

    @ColumnInfo(name = "participants")
    val participants: List<Participant>
)
