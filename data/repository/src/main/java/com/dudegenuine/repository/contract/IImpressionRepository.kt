package com.dudegenuine.repository.contract

import com.dudegenuine.model.Impression

/**
 * Wed, 06 Jul 2022
 * WhoKnows by utifmd
 **/
interface IImpressionRepository {
    suspend fun create(impression: Impression): Impression
    suspend fun update(id: String, impression: Impression): Impression
}