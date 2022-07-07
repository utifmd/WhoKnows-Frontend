package com.dudegenuine.local.mapper

import androidx.room.TypeConverter
import com.dudegenuine.model.Impression
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Thu, 07 Jul 2022
 * WhoKnows by utifmd
 **/
object ImpressionsConverter {
    @TypeConverter
    fun toJson(data: List<Impression>): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun fromJson(data: String): List<Impression> {
        val type = object : TypeToken<List<Impression?>?>(){}.type
        return Gson().fromJson(data, type)
    }
}