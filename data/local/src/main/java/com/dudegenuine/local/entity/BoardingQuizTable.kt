package com.dudegenuine.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Thu, 17 Feb 2022
 * WhoKnows by utifmd
 **/
@Entity
data class BoardingQuizTable(
    @PrimaryKey
    @ColumnInfo(name = "participantId")
    val participantId: String,

    @ColumnInfo(name = "participantName")
    val participantName: String,

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

    @ColumnInfo(name = "currentQuestionIdx")
    val currentQuestionIdx: Int,

    @ColumnInfo(name = "quizzes")
    val quizzes: List<OnBoardingStateTable>
)

