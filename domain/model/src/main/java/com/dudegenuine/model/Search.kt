package com.dudegenuine.model

/**
 * Tue, 14 Jun 2022
 * WhoKnows by utifmd
 **/
sealed class Search<T>(val data: T? = null) {

    class Room<T>(data: T): Search<T>(data)
    class User<T>(data: T): Search<T>(data)
}