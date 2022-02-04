package com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.model.Answer
import com.dudegenuine.model.PossibleAnswer
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.QuizActionType
import com.dudegenuine.model.common.ImageUtil.strOf
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.contract.IQuizPrivateEvent
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.contract.IQuizPrivateState

/**
 * Fri, 24 Dec 2021
 * WhoKnows by utifmd
 **/
@ExperimentalCoilApi
@Composable
fun QuestionBoardingScreen(
    modifier: Modifier = Modifier,
    quiz: Quiz,
    exactAnswer: Answer?,
    onAnswer: (Answer) -> Unit,
    onAction: (Int, QuizActionType) -> Unit) {

    val exactEvent = object: IQuizPrivateEvent {
        override fun onAnswerSelected(freshAnswer: String) {
            onAnswer(Answer(
                type = strOf<PossibleAnswer.SingleChoice>(),
                answer = freshAnswer
            ))
        }

        override fun onAnswerSelected(freshAnswer: String, selected: Boolean) {
            if (exactAnswer == null) {
                onAnswer(Answer(
                    type = strOf<PossibleAnswer.MultipleChoice>(),
                    answers = setOf(freshAnswer)))
            } else {
                val newAnswers = exactAnswer.answers?.toMutableSet()

                if (!selected) newAnswers?.remove(freshAnswer)
                else newAnswers?.add(freshAnswer)

                onAnswer(Answer(
                    type = strOf<PossibleAnswer.MultipleChoice>(),
                    answers = newAnswers)
                )
            }
        }

        override fun onAction(id: Int, type: QuizActionType) = onAction(id, type)
    }

    QuizScreen(
        contentModifier = modifier,
        stateCompose = object: IQuizPrivateState {
            override val model: Quiz = quiz
            override val answer: Answer? = exactAnswer
            override val event: IQuizPrivateEvent = exactEvent
        },
    )
}