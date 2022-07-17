package com.dudegenuine.whoknows.infrastructure.di.android.dependency

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.StringRes
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.dudegenuine.repository.contract.dependency.local.IResourceDependency
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ux.activity.MainActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Wed, 01 Jun 2022
 * WhoKnows by utifmd
 **/
@SuppressLint("UnspecifiedImmutableFlag")
class ResourceDependency(
    private val context: Context): IResourceDependency {
    override val appName: String  = string(R.string.app_name)
    override val appIcon: Bitmap? = drawable(R.mipmap.ic_launcher)?.toBitmap()
    override val statusBarSmallIconId: Int = R.drawable.ic_outline_fact_check_24
    private val imageRequestBuilder = ImageRequest.Builder(context).allowHardware(false)
    private val imageLoader = ImageLoader(context)

    override fun bitmapFlow(url: String): Flow<Bitmap> = flow {
        val request = imageRequestBuilder.data(url).build()
        when (val response = imageLoader.execute(request)){
            is SuccessResult -> emit(response.drawable.toBitmap())
            is ErrorResult -> appIcon?.let{ emit(it) }
        }
    }
    override suspend fun bitmapAsync(url: String, onCompletion: (Bitmap) -> Unit) {
        val request = imageRequestBuilder.data(url).build()
        when (val response = imageLoader.execute(request)){
            is SuccessResult -> onCompletion(response.drawable.toBitmap())
            is ErrorResult -> appIcon?.let{ onCompletion(it) }
        }
    }
    override fun string(@StringRes id: Int): String = context.getString(id)
    override fun drawable(id: Int): Drawable? = context.getDrawable(id)
    override fun mainActivityIntent(data: String): Intent = MainActivity.instance(context, data)
}