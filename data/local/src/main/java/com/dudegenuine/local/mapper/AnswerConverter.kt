package com.dudegenuine.local.mapper

import androidx.room.TypeConverter
import com.dudegenuine.model.Answer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Tue, 22 Feb 2022
 * WhoKnows by utifmd
 **/
object AnswerConverter {
    @TypeConverter
    fun fromJson(data: String?): Answer? {
        val type = object: TypeToken<Answer?>(){}.type
        return Gson().fromJson(data, type)
    }

    @TypeConverter
    fun toJson(data: Answer?): String? {
        return Gson().toJson(data)
    }
}