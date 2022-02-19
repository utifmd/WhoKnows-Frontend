package com.dudegenuine.local.mapper

import androidx.room.TypeConverter
import com.dudegenuine.local.entity.CurrentRoomState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Sat, 19 Feb 2022
 * WhoKnows by utifmd
 **/
object BoardingConverter {
    @TypeConverter
    fun fromJson(data: String): List<CurrentRoomState.BoardingQuiz> {
        val type: Type = object : TypeToken<List<CurrentRoomState.BoardingQuiz?>?>() {}.type

        return Gson().fromJson(data, type)
    }

    @TypeConverter
    fun toJson(boardings: List<CurrentRoomState.BoardingQuiz>): String {

        return Gson().toJson(boardings)
    }
}