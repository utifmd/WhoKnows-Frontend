package com.dudegenuine.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dudegenuine.model.User

/**
 * Thu, 17 Feb 2022
 * WhoKnows by utifmd
 **/
@Entity(tableName = "participation")
data class ParticipationTable(
    @PrimaryKey
    @ColumnInfo(name = "participantId")
    val participantId: String,

    @ColumnInfo(name = "user")
    val user: User.Censored,

    @ColumnInfo(name = "userId")
    val userId: String,

    @ColumnInfo(name = "roomId")
    val roomId: String,

    @ColumnInfo(name = "roomTitle")
    val roomTitle: String,

    @ColumnInfo(name = "roomDesc")
    val roomDesc: String,

    @ColumnInfo(name = "roomMinute")
    val roomMinute: Int,

    @ColumnInfo(name = "roomToken")
    val roomToken: String,

    @ColumnInfo(name = "roomRecipientIds")
    val roomRecipientIds: List<String>,

    @ColumnInfo(name = "currentQuestionIdx")
    val currentQuestionIdx: Int,

    @ColumnInfo(name = "pages")
    val pages: List<ParticipationPageTable>
)

