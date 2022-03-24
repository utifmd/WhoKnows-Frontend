package com.dudegenuine.local.mapper

import androidx.room.TypeConverter
import com.dudegenuine.model.Quiz
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Tue, 22 Feb 2022
 * WhoKnows by utifmd
 **/
object AnswerConverter {
    @TypeConverter
    fun fromJson(data: String?): Quiz.Answer.Exact? {
        val type = object: TypeToken<Quiz.Answer.Exact?>(){}.type
        return Gson().fromJson(data, type)
    }

    @TypeConverter
    fun toJson(data: Quiz.Answer.Exact?): String? {
        return Gson().toJson(data)
    }
}