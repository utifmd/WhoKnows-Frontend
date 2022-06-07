package com.dudegenuine.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.dudegenuine.model.Quiz

/**
 * Mon, 21 Feb 2022
 * WhoKnows by utifmd
 **/
@Entity
data class ParticipationPageTable(
    @ColumnInfo(name = "quiz")
    val quiz: QuizTable,

    @ColumnInfo(name = "questionIndex")
    val questionIndex: Int,

    @ColumnInfo(name = "totalQuestionsCount")
    val totalQuestionsCount: Int,

    @ColumnInfo(name = "showPrevious")
    val showPrevious: Boolean,

    @ColumnInfo(name = "showDone")
    val showDone: Boolean,

    @ColumnInfo(name = "enableNext")
    val enableNext: Boolean,

    @ColumnInfo(name = "answer")
    val answer: Quiz.Answer.Exact?,

    @ColumnInfo(name = "isCorrect")
    val isCorrect: Boolean,
)