package com.dudegenuine.whoknows.infrastructure.di.android.dependency

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.dudegenuine.repository.contract.dependency.local.IClipboardManager

/**
 * Sat, 29 Jan 2022
 * WhoKnows by utifmd
 **/
class ClipboardManager(
    private val context: Context): IClipboardManager {
    private val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    override fun applyPlainText(label: String, value: String) {
        val clipData = ClipData.newPlainText(label, value)

        clipboard.setPrimaryClip(clipData).also {
            Toast.makeText(context, "Room ID copied!", Toast.LENGTH_SHORT).show()
        }

        Log.d("applyPlainText: ","triggered")
    }
}