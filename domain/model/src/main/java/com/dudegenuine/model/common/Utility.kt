package com.dudegenuine.model.common

/**
 * Tue, 21 Dec 2021
 * WhoKnows by utifmd
 **/
object Utility {
    val TAG = strOf<Utility>()
    inline fun< reified T> strOf(): String = T::class.java.simpleName
}