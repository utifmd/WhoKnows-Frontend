package com.dudegenuine.whoknows.ux.compose.state

import com.dudegenuine.model.Result
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IAppUseCaseModule.Companion.EMPTY_STRING
import java.util.*

/**
 * Sun, 26 Jun 2022
 * WhoKnows by utifmd
 **/
object ResultState {
    operator fun invoke(): Result = Result(
        resultId = "RSL-${UUID.randomUUID()}",
        roomId = EMPTY_STRING,
        userId = EMPTY_STRING,
        correctQuiz = emptyList(),
        wrongQuiz = emptyList(),
        score = 0,
        createdAt = Date(),
        updatedAt = null,
        user = null,
    )
}