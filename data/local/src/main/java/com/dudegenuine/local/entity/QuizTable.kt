package com.dudegenuine.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.*

/**
 * Mon, 21 Feb 2022
 * WhoKnows by utifmd
 **/
@Entity
data class QuizTable(
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "roomId")
    var roomId: String,

    @ColumnInfo(name = "images")
    var images: List<String>,

    @ColumnInfo(name = "question")
    var question: String,

    @ColumnInfo(name = "options")
    var options: List<String>,

    @ColumnInfo(name = "answer")
    var answer: String,

    @ColumnInfo(name = "createdBy")
    var createdBy: String,

    @ColumnInfo(name = "createdAt")
    var createdAt: Date,

    @ColumnInfo(name = "updatedAt")
    var updatedAt: Date?
)
