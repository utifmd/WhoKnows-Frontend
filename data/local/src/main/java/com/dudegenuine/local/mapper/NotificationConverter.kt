package com.dudegenuine.local.mapper

import androidx.room.TypeConverter
import com.dudegenuine.model.Notification
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Sat, 19 Mar 2022
 * WhoKnows by utifmd
 **/
object NotificationConverter {
    @TypeConverter
    fun fromJson(data: String): List<Notification> {
        val type: Type = object : TypeToken<List<Notification?>?>() {}.type

        return Gson().fromJson(data, type)
    }

    @TypeConverter
    fun toJson(notifications: List<Notification>): String {
        return Gson().toJson(notifications)
    }
}