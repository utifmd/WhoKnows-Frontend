package com.dudegenuine.repository.contract.dependency.local

import androidx.annotation.StringRes

/**
 * Wed, 01 Jun 2022
 * WhoKnows by utifmd
 **/
interface IResourceDependency {
    fun string(@StringRes id: Int): String
}