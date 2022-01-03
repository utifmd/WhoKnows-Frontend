package com.dudegenuine.model.common

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

/**
 * Tue, 21 Dec 2021
 * WhoKnows by utifmd
 **/
object Utility {
    val TAG = strOf<Utility>()
    inline fun <reified T> strOf(): String = T::class.java.simpleName

    fun asBase64 (bitmap: Bitmap): String = ByteArrayOutputStream().use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, it)

        Base64.encodeToString(it.toByteArray(), Base64.DEFAULT)
    }

    fun asBitmap (base64: String): Bitmap {
        val byteArray: ByteArray = Base64.decode(base64, Base64.DEFAULT)

        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}