package com.dudegenuine.local.mapper

import android.util.Log
import androidx.room.TypeConverter
import com.dudegenuine.local.entity.OnBoardingStateTable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Sat, 19 Feb 2022
 * WhoKnows by utifmd
 **/
object BoardingConverter {
    private val TAG = javaClass.simpleName

    @TypeConverter
    fun fromJson(data: String): List<OnBoardingStateTable> {
        val type: Type = object : TypeToken<List<OnBoardingStateTable?>?>() {}.type

        Log.d(TAG, "fromJson: triggered")

        return Gson().fromJson(data, type)
    }

    @TypeConverter
    fun toJson(onBoardings: List<OnBoardingStateTable>): String {

        return Gson().toJson(onBoardings)
    }
}