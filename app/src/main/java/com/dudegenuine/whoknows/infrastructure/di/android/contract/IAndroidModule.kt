package com.dudegenuine.whoknows.infrastructure.di.android.contract

import androidx.lifecycle.SavedStateHandle

/**
 * Fri, 03 Dec 2021
 * WhoKnows by utifmd
 **/
interface IAndroidModule {
    fun provideSavedStateHandleModule(): SavedStateHandle
}