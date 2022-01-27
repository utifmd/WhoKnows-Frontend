package com.dudegenuine.model.common

import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
object ViewUtil {
    fun timeAgo(source: Date): String {
        return try {
            /*val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)*/ /*val time = formatter.parse(source).time*/
            val time = source.time
            val now = System.currentTimeMillis()
            val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)

            ago.toString()
        } catch (e: Exception){
            source.toString()
        }
    }

    fun pretty(source: String): String {
        return try {
            val sdf: (String) -> SimpleDateFormat = { SimpleDateFormat(it).apply {
                timeZone = TimeZone.getTimeZone("GMT")
            }}

            val pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
            val time = sdf(pattern).parse(source).time
            val now = System.currentTimeMillis()
            val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)

            ago.toString()
        } catch (e: ParseException) {
            e.printStackTrace()
            source
        }
    }

}