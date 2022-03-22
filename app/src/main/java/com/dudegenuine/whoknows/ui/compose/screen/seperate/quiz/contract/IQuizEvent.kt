package com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.contract

import com.dudegenuine.model.Answer
import com.dudegenuine.model.QuizActionType

/**
 * Mon, 31 Jan 2022
 * WhoKnows by utifmd
 **/
interface IQuizEvent {
    fun onPicturePressed(fileId: String?) {}
}

interface IQuizPrivateEvent: IQuizEvent {
    fun onAnswer(answer: Answer){}
    fun onAction(id: Int, type: QuizActionType){}

    fun onAnswerSelected(freshAnswer: String){}
    fun onAnswerSelected(freshAnswer: String, selected: Boolean){}
}

interface IQuizPublicEvent: IQuizEvent{
    fun onBackPressed()
    fun onDeletePressed()
}
