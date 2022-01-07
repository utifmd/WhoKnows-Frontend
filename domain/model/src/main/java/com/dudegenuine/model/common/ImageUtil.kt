package com.dudegenuine.model.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream


/**
 * Tue, 21 Dec 2021
 * WhoKnows by utifmd
 **/
object ImageUtil {
    val TAG = strOf<ImageUtil>()
    inline fun <reified T> strOf(): String = T::class.java.simpleName

    fun asBase64 (bitmap: Bitmap): String = ByteArrayOutputStream().use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, it)

        Base64.encodeToString(it.toByteArray(), Base64.DEFAULT)
    }

    fun asBitmap (base64: String): Bitmap {
        val byteArray: ByteArray = Base64.decode(base64, Base64.DEFAULT)

        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    val asBitmap: (ByteArray) -> Bitmap = {
        BitmapFactory.decodeByteArray(it, 0, it.size)
    }

    val adjustImage: (Context, Uri) -> ByteArray = { context, uri ->
        val input = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(input)

        val nh = (bitmap.height * (512.0 / bitmap.width)).toInt()
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 512, nh, true)
        val stream = ByteArrayOutputStream().apply {
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, this)
        }

        stream.toByteArray()
    }
}