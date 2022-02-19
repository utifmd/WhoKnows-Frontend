package com.dudegenuine.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dudegenuine.model.Quiz

/**
 * Thu, 17 Feb 2022
 * WhoKnows by utifmd
 **/
@Entity
data class CurrentRoomState(
    @PrimaryKey
    @ColumnInfo(name = "participantId")
    val participantId: String,

    @ColumnInfo(name = "participantName")
    val participantName: String,

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
    val quizzes: List<BoardingQuiz>){

    data class BoardingQuiz(
        val quiz: Quiz,
        val questionIndex: Int,
        val totalQuestionsCount: Int,
        val showPrevious: Boolean,
        val showDone: Boolean
    )
}

