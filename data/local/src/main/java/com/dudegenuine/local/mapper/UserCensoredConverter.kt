package com.dudegenuine.local.mapper

import androidx.room.TypeConverter
import com.dudegenuine.model.UserCensored
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Tue, 22 Feb 2022
 * WhoKnows by utifmd
 **/
object UserCensoredConverter {
    @TypeConverter
    fun fromJson(data: String): UserCensored?{
        val type = object: TypeToken<UserCensored?>(){}.type

        return Gson().fromJson(data, type)
    }
    @TypeConverter
    fun toJson(data: UserCensored?): String? {
        return Gson().toJson(data)
    }
}