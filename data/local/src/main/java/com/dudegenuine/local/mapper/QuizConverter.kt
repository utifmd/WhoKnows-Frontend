package com.dudegenuine.local.mapper

import androidx.room.TypeConverter
import com.dudegenuine.local.entity.QuizTable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Mon, 21 Feb 2022
 * WhoKnows by utifmd
 **/
object QuizConverter {
    private val TAG = javaClass.simpleName

    @TypeConverter
    fun fromJson(data: String): QuizTable? {
        val type = object: TypeToken<QuizTable?>(){}.type

        //Log.d(TAG, "fromJson: triggered")

        return Gson().fromJson(data, type)
    }
    @TypeConverter
    fun toJson(data: QuizTable?): String? {
        return Gson().toJson(data)
    }
}