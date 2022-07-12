package com.dudegenuine.whoknows.ux.compose.state

import com.dudegenuine.model.Notification
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IAppUseCaseModule.Companion.EMPTY_STRING
import java.util.*

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
object NotificationState {
    operator fun invoke(): Notification = Notification(
        "NTF-${UUID.randomUUID()}",
        EMPTY_STRING,
        EMPTY_STRING,
        "Just follow you",
        false,
        "",
        true,
        Date(),
        null,
        null
    )
}
