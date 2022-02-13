package com.dudegenuine.whoknows.infrastructure.common.extension

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * Sat, 12 Feb 2022
 * WhoKnows by utifmd
 **/

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}