package com.dudegenuine.whoknows.infrastructure.di.android.dependency

import android.content.Context
import androidx.annotation.StringRes
import com.dudegenuine.repository.contract.dependency.local.IResourceDependency

/**
 * Wed, 01 Jun 2022
 * WhoKnows by utifmd
 **/
class ResourceDependency(
    private val context: Context): IResourceDependency {
    override fun string(@StringRes id: Int): String {
        return context.getString(id)
    }
}