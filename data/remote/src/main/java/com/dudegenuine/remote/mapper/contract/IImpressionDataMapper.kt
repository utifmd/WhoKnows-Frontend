package com.dudegenuine.remote.mapper.contract

import com.dudegenuine.model.Impression
import com.dudegenuine.remote.entity.ImpressionEntity
import com.dudegenuine.remote.entity.Response

/**
 * Mon, 30 May 2022
 * WhoKnows by utifmd
 **/
interface IImpressionDataMapper {
    fun asEntity(impression: Impression): ImpressionEntity
    fun asImpression(entity: ImpressionEntity): Impression
    fun asImpression(response: Response<ImpressionEntity>): Impression
    fun asImpressions(response: Response<List<ImpressionEntity>>): List<Impression>
}