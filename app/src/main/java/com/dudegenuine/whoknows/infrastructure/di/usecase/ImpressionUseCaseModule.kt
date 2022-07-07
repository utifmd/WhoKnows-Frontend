package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.contract.IImpressionRepository
import com.dudegenuine.repository.contract.IMessagingRepository
import com.dudegenuine.repository.contract.INotificationRepository
import com.dudegenuine.usecase.room.OperateImpression
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IImpressionUseCaseModule

/**
 * Wed, 06 Jul 2022
 * WhoKnows by utifmd
 **/
class ImpressionUseCaseModule(
    private val reposImpression: IImpressionRepository,
    private val reposNotification: INotificationRepository,
    private val reposMessaging: IMessagingRepository,

    override val operateImpression: OperateImpression = OperateImpression(
        repoImpression = reposImpression,
        repoNotify = reposNotification,
        repoMessaging = reposMessaging,
    )
) : IImpressionUseCaseModule