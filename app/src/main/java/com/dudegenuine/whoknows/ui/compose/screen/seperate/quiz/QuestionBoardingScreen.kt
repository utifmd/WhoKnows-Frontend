package com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.common.ImageUtil.strOf
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.contract.IQuizPrivateEvent
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.contract.IQuizPrivateState

/**
 * Fri, 24 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun QuestionBoardingScreen(
    modifier: Modifier = Modifier,
    quiz: Quiz.Complete,
    exactAnswer: Quiz.Answer.Exact?,
    onAnswer: (Quiz.Answer.Exact) -> Unit,
    onAction: (Int, Quiz.Action.Type) -> Unit) {

    val exactEvent = object: IQuizPrivateEvent {
        override fun onAnswerSelected(freshAnswer: String) {
            onAnswer(Quiz.Answer.Exact(
                type = strOf<Quiz.Answer.Possible.SingleChoice>(),
                answer = freshAnswer
            ))
        }

        override fun onAnswerSelected(freshAnswer: String, selected: Boolean) {
            if (exactAnswer == null) {
                onAnswer(Quiz.Answer.Exact(
                    type = strOf<Quiz.Answer.Possible.MultipleChoice>(),
                    answers = setOf(freshAnswer)))
            } else {
                val newAnswers = exactAnswer.answers?.toMutableSet()

                if (!selected) newAnswers?.remove(freshAnswer)
                else newAnswers?.add(freshAnswer)

                onAnswer(Quiz.Answer.Exact(
                    type = strOf<Quiz.Answer.Possible.MultipleChoice>(),
                    answers = newAnswers)
                )
            }
        }

        override fun onAction(id: Int, type: Quiz.Action.Type) = onAction(id, type)
    }

    QuizScreen(
        contentModifier = modifier,
        stateCompose = object: IQuizPrivateState {
            override val model: Quiz.Complete = quiz
            override val answer: Quiz.Answer.Exact? = exactAnswer
            override val event: IQuizPrivateEvent = exactEvent
        },
    )
}