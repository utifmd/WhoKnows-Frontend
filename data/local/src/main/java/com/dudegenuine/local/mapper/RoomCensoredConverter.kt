package com.dudegenuine.local.mapper

import androidx.room.TypeConverter
import com.dudegenuine.model.Room
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Thu, 21 Jul 2022
 * WhoKnows by utifmd
 **/
object RoomCensoredConverter {
    @TypeConverter
    fun fromJson(data: String?): Room.Censored {
        val type = object: TypeToken<Room.Censored?>(){}.type
        return Gson().fromJson(data, type)
    }

    @TypeConverter
    fun toJson(data: Room.Censored?): String {
        return Gson().toJson(data)
    }
}