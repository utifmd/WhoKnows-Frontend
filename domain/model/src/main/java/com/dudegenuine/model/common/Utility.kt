package com.dudegenuine.model.common

import android.os.Build

/**
 * Sat, 12 Feb 2022
 * WhoKnows by utifmd
 **/
object Utility {
    val isOreoCompatibility: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
}