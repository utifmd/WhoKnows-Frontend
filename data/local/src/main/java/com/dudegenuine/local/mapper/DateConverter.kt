package com.dudegenuine.local.mapper

import androidx.room.TypeConverter
import java.util.*

/**
 * Sat, 19 Feb 2022
 * WhoKnows by utifmd
 **/
object DateConverter {
    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}