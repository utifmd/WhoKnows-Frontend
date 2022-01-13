package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.dudegenuine.model.Answer
import com.dudegenuine.model.PossibleAnswer
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.QuizActionType
import com.dudegenuine.model.common.ImageUtil.asBitmap
import com.dudegenuine.model.common.ImageUtil.strOf
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.MultipleChoiceQuestion
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.SingleChoiceQuestion

/**
 * Fri, 24 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun QuestionScreen(
    modifier: Modifier = Modifier,
    quiz: Quiz,
    answer: Answer?,
    onAnswer: (Answer) -> Unit,
    onAction: (Int, QuizActionType) -> Unit) {
    LazyColumn(
        modifier = modifier.fillMaxSize(), //.padding(innerPadding),
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp
        )) {
        item {
            Spacer(
                modifier = modifier.height(44.dp)
            )
            val backgroundColor = MaterialTheme.colors.onSurface.copy(
                alpha = if(MaterialTheme.colors.isLight) 0.04f else 0.06f
            )
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = modifier.fillMaxWidth()){

                quiz.images.map { url ->
                    Image(
                        painter = rememberImagePainter(
                            data = if(url.contains("://")) url else asBitmap(url),
                            builder = {
                                crossfade(true)
                            }
                        ),
                        contentDescription = url.split("://")[1],
                        modifier = modifier.size(128.dp)
                    )
                }
            }
            Spacer(modifier = modifier.height(8.dp))
            Row(
                modifier = modifier.fillMaxWidth().background(
                        color = backgroundColor,
                        shape = MaterialTheme.shapes.small
                    )) {
                Text(
                    text = quiz.question,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = modifier.fillMaxWidth().padding(
                        vertical = 24.dp,
                        horizontal = 16.dp
                    )
                )
            }
            Spacer(modifier = modifier.height(24.dp))
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = "Created at ${quiz.createdAt} by ${quiz.createdBy}",
                    style = MaterialTheme.typography.caption,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = 24.dp,
                            start = 8.dp,
                            end = 8.dp
                        )
                )
            }
            when(quiz.answer) {
                is PossibleAnswer.SingleChoice -> SingleChoiceQuestion(
                    options = quiz.options,
                    answer = answer,
                    onAnswerSelected = { newAnswer -> onAnswer(
                        Answer(
                            type = strOf<PossibleAnswer.SingleChoice>(),
                            answer = newAnswer
                        )
                    )}
                )
                is PossibleAnswer.MultipleChoice -> MultipleChoiceQuestion(
                    options = quiz.options,
                    answer = answer,
                    onAnswerSelected = { newAnswer, selected ->
                        if (answer == null) {
                            onAnswer(Answer(type = strOf<PossibleAnswer.MultipleChoice>(), answers = setOf(newAnswer)))
                        } else {
                            val newAnswers = answer.answers?.toMutableSet()
                            
                            if (!selected) newAnswers?.remove(newAnswer)
                            else newAnswers?.add(newAnswer)

                            onAnswer(Answer(type = strOf<PossibleAnswer.MultipleChoice>(), answers = newAnswers))
                        }
                    }
                )
                else -> return@item
            }
        }
    }
}