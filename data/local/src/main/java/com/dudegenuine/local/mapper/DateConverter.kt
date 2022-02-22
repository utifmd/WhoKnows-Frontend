package com.dudegenuine.local.mapper

import android.util.Log
import androidx.room.TypeConverter
import java.util.*

/**
 * Sat, 19 Feb 2022
 * WhoKnows by utifmd
 **/
object DateConverter {
    private val TAG = javaClass.simpleName

    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        Log.d(TAG, "toDate: triggered")

        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}