package com.dudegenuine.remote.mapper

import com.dudegenuine.model.Impression
import com.dudegenuine.remote.entity.ImpressionEntity
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.mapper.contract.IImpressionDataMapper

/**
 * Mon, 30 May 2022
 * WhoKnows by utifmd
 **/
class ImpressionDataMapper: IImpressionDataMapper {

    override fun asEntity(impression: Impression): ImpressionEntity =
        ImpressionEntity(impression.impressionId, impression.postId, impression.userId, impression.good)

    override fun asImpression(entity: ImpressionEntity): Impression =
        Impression(entity.impressionId, entity.postId, entity.userId, entity.good)

    override fun asImpression(response: Response<ImpressionEntity>): Impression {
        return when(response.data){
            is ImpressionEntity -> response.data.let { impression ->
                Impression(impression.impressionId, impression.postId, impression.userId, impression.good)
            }
            else -> throw IllegalStateException()
        }
    }

    override fun asImpressions(response: Response<List<ImpressionEntity>>): List<Impression> {
        return when(response.data){
            is List<*> -> response.data
                .filterIsInstance<ImpressionEntity>()
                .map(::asImpression)

            else -> throw IllegalStateException()
        }
    }
}