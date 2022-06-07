package com.dudegenuine.local.mapper

import androidx.room.TypeConverter
import com.dudegenuine.model.Quiz
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Tue, 31 May 2022
 * WhoKnows by utifmd
 **/
object QuizzesConverter {
    @TypeConverter
    fun toJson(data: List<Quiz.Complete>): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun fromJson(data: String): List<Quiz.Complete> {
        val type = object: TypeToken<List<Quiz.Complete?>?>(){}.type

        return Gson().fromJson(data, type)
    }
}