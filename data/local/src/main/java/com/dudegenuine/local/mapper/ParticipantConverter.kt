package com.dudegenuine.local.mapper

import androidx.room.TypeConverter
import com.dudegenuine.model.Participant
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Sat, 19 Feb 2022
 * WhoKnows by utifmd
 **/
object ParticipantConverter {
    private val TAG = javaClass.simpleName

    @TypeConverter
    fun fromJson(data: String): List<Participant> {
        val type: Type = object : TypeToken<List<Participant?>?>() {}.type

        //Log.d(TAG, "fromJson: triggered")

        return Gson().fromJson(data, type)
    }

    @TypeConverter
    fun toJson(users: List<Participant>): String {
        return Gson().toJson(users)
    }
}