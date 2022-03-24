package com.dudegenuine.local.mapper

import androidx.room.TypeConverter
import com.dudegenuine.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Tue, 22 Feb 2022
 * WhoKnows by utifmd
 **/
object UserCensoredConverter {
    @TypeConverter
    fun fromJson(data: String): User.Censored?{
        val type = object: TypeToken<User.Censored?>(){}.type

        return Gson().fromJson(data, type)
    }
    @TypeConverter
    fun toJson(data: User.Censored?): String? {
        return Gson().toJson(data)
    }
}