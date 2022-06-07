package com.dudegenuine.local.mapper

import androidx.room.TypeConverter
import com.dudegenuine.local.entity.ParticipationPageTable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Sat, 19 Feb 2022
 * WhoKnows by utifmd
 **/
object ParticipationPageConverter {
    private val TAG = javaClass.simpleName

    @TypeConverter
    fun fromJson(data: String): List<ParticipationPageTable> {
        val type: Type = object : TypeToken<List<ParticipationPageTable?>?>() {}.type

        //Log.d(TAG, "fromJson: triggered")

        return Gson().fromJson(data, type)
    }

    @TypeConverter
    fun toJson(onBoardings: List<ParticipationPageTable>): String {

        return Gson().toJson(onBoardings)
    }
}