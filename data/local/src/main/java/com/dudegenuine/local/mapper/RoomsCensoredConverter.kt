package com.dudegenuine.local.mapper

import androidx.room.TypeConverter
import com.dudegenuine.model.Room
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Tue, 01 Mar 2022
 * WhoKnows by utifmd
 **/
object RoomsCensoredConverter {
    @TypeConverter
    fun fromJson(data: String?): List<Room.Censored?>?{
        val type = object: TypeToken<List<Room.Censored?>?>(){}.type

        return Gson().fromJson(data, type)
    }
    @TypeConverter
    fun toJson(data: List<Room.Censored>?): String?{

        return Gson().toJson(data)
    }
}