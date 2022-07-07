package com.dudegenuine.whoknows.ux.compose.state

import com.dudegenuine.model.Participant
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IAppUseCaseModule.Companion.EMPTY_STRING
import java.util.*

/**
 * Sun, 26 Jun 2022
 * WhoKnows by utifmd
 **/
object ParticipantState {
    operator fun invoke(): Participant = Participant(
        id = "PPN-${UUID.randomUUID()}",
        roomId = EMPTY_STRING,
        userId = EMPTY_STRING,
        currentPage = EMPTY_STRING,
        timeLeft = null,
        expired = false,
        createdAt = Date(),
        updatedAt = null,
        user = null,
        isCurrentUser = false
    )
}