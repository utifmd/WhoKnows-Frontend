package com.dudegenuine.local.mapper

import androidx.room.TypeConverter
import com.dudegenuine.model.RoomCensored
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Tue, 01 Mar 2022
 * WhoKnows by utifmd
 **/
object RoomCensoredConverter {
    @TypeConverter
    fun fromJson(data: String?): List<RoomCensored?>?{
        val type = object: TypeToken<List<RoomCensored?>?>(){}.type

        return Gson().fromJson(data, type)
    }
    @TypeConverter
    fun toJson(data: List<RoomCensored>?): String?{

        return Gson().toJson(data)
    }
}