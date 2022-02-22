package com.dudegenuine.local.mapper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Mon, 21 Feb 2022
 * WhoKnows by utifmd
 **/
object ListStringConverter {
    @TypeConverter
    fun fromJson(data: String): List<String>{
        val type = object: TypeToken<List<String?>?>(){}.type

        return Gson().fromJson(data, type)
    }
    @TypeConverter
    fun toJson(data: List<String>): String{
        return Gson().toJson(data)
    }
}