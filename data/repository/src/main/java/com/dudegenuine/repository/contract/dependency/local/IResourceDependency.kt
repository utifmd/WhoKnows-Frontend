package com.dudegenuine.repository.contract.dependency.local

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.coroutines.flow.Flow

/**
 * Wed, 01 Jun 2022
 * WhoKnows by utifmd
 **/
interface IResourceDependency {
    val appName: String
    val appIcon: Bitmap?
    val statusBarSmallIconId: Int

    fun string(@StringRes id: Int): String
    fun drawable(@DrawableRes id: Int): Drawable?
    fun mainActivityIntent(data: String): Intent
    fun bitmapFlow(url: String): Flow<Bitmap>
    suspend fun bitmapAsync(url: String, onCompletion: (Bitmap) -> Unit)
}