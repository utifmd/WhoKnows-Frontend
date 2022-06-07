package com.dudegenuine.whoknows.ux.compose.screen.seperate.quiz.contract

import com.dudegenuine.model.Quiz

/**
 * Mon, 31 Jan 2022
 * WhoKnows by utifmd
 **/
interface IQuizEvent {
    fun onPicturePressed(fileId: String?) {}
}

interface IQuizPrivateEvent: IQuizEvent {
    fun onAnswer(answer: Quiz.Answer.Exact){}
    fun onAction(id: Int, type: Quiz.Action.Type){}

    fun onAnswerSelected(freshAnswer: String){}
    fun onAnswerSelected(freshAnswer: String, selected: Boolean){}
}

interface IQuizPublicEvent: IQuizEvent{
    fun onBackPressed()
    fun onDeletePressed(){}
}
