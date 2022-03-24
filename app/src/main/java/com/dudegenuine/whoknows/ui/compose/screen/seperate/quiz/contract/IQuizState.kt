package com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.contract

import com.dudegenuine.model.Quiz

/**
 * Mon, 31 Jan 2022
 * WhoKnows by utifmd
 **/
interface IQuizState {
    companion object {
        const val QUIZ_ID_SAVED_KEY = "quiz_is_saved_key"
    }
}


interface IQuizPrivateState: IQuizState {
    val model: Quiz.Complete
    val answer: Quiz.Answer.Exact?
    val event: IQuizPrivateEvent
}

interface IQuizPublicState: IQuizState {
    val event: IQuizPublicEvent
}
