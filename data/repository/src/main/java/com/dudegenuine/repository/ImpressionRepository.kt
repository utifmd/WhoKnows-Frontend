package com.dudegenuine.repository

import com.dudegenuine.model.Impression
import com.dudegenuine.remote.mapper.contract.IImpressionDataMapper
import com.dudegenuine.remote.service.contract.IImpressionService
import com.dudegenuine.repository.contract.IImpressionRepository
import com.dudegenuine.repository.contract.dependency.local.IPreferenceManager
import javax.inject.Inject

/**
 * Wed, 06 Jul 2022
 * WhoKnows by utifmd
 **/
class ImpressionRepository
    @Inject constructor(
    private val service: IImpressionService,
    private val mapper: IImpressionDataMapper,
    private val prefs: IPreferenceManager): IImpressionRepository {

    override suspend fun create(impression: Impression): Impression {
        return mapper.asImpression(service.create(mapper.asEntity(impression)))
    }

    override suspend fun update(id: String, impression: Impression): Impression {
        return mapper.asImpression(service.update(id, mapper.asEntity(impression)))
    }
}