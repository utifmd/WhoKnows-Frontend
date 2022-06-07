package com.dudegenuine.whoknows.ux.compose.screen.seperate.participation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dudegenuine.model.Quiz
import com.dudegenuine.repository.contract.dependency.local.ITimerService.Companion.TIME_ACTION
import com.dudegenuine.repository.contract.dependency.local.ITimerService.Companion.asString
import com.dudegenuine.whoknows.ux.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ux.compose.screen.LoadingScreen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.quiz.QuestionBoardingScreen
import com.dudegenuine.whoknows.ux.vm.participation.ParticipationViewModel

/**
 * Fri, 17 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
@OptIn(ExperimentalMaterialApi::class)
fun ParticipationScreen(
    modifier: Modifier = Modifier,
    viewModel: ParticipationViewModel) {
    viewModel.state.participation?.let { state ->
        val context = LocalContext.current
        val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
        val boardingState = remember(state.currentQuestionIdx) {
            state.pages[state.currentQuestionIdx]
        }
        DisposableEffect(context, TIME_ACTION) {
            val intention = viewModel.receiver.timerIntent
            val receiver = state.let(viewModel::onTimerReceiver)

            context.registerReceiver(receiver, intention)
            onDispose {
                with(viewModel){
                    if (boardingState.questionIndex != state.currentQuestionIdx) patchBoarding(state)
                    else postBoarding(state)
                }
                context.unregisterReceiver(receiver)
            }
        }
        with(viewModel.state) {
            if (error.isNotBlank()) ErrorScreen(message = error)
        }
        if (viewModel.state.loading) LoadingScreen()
        else BackdropScaffold(
            scaffoldState = scaffoldState,
            appBar = {
                Row(modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    if (boardingState.showPrevious) {
                        Spacer(modifier.size(16.dp))
                        TextButton(onClick = viewModel::onBoardingPrevPressed) {
                            Text("Previous", color = MaterialTheme.colors.onPrimary)
                        }
                    }
                    Text("${asString(viewModel.timer)} time left",
                        style = MaterialTheme.typography.h6,
                        modifier = modifier.padding(
                            horizontal = 16.dp
                        )
                    )
                    Row {
                        if (boardingState.showDone) {
                            TextButton(
                                enabled = boardingState.enableNext,
                                onClick = viewModel::onBoardingDonePressed) {
                                Text(text = "Done", color = if(boardingState.enableNext) MaterialTheme.colors.onPrimary
                                    else MaterialTheme.colors.onPrimary.copy(0.5f))
                            }
                        } else {
                            TextButton(
                                enabled = boardingState.enableNext,
                                onClick = viewModel::onBoardingNextPressed) {
                                Text(text = "Next", color = if(boardingState.enableNext) MaterialTheme.colors.onPrimary
                                    else MaterialTheme.colors.onPrimary.copy(0.5f))
                            }
                        }
                    }
                }
            },

            backLayerContent = {
                Column(
                    modifier = modifier.padding(12.dp)) {
                    listOf(state.roomTitle, state.roomDesc, "${state.roomMinute} minute\'s").forEach {

                        Text(it, modifier = modifier.padding(vertical = 8.dp, horizontal = 12.dp))
                    }
                }
           },

            frontLayerContent = {
                Column(
                    modifier = modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween) {

                    Text("Question number ${boardingState.questionIndex} from ${boardingState.totalQuestionsCount}", style = MaterialTheme.typography.caption, modifier = modifier.padding(top = 24.dp))

                    LinearProgressIndicator(
                        progress = boardingState.questionIndex.toFloat() / boardingState.totalQuestionsCount.toFloat(),
                        modifier = modifier
                            .requiredWidth(126.dp)
                            .padding(12.dp),
                        color = MaterialTheme.colors.primaryVariant,
                        backgroundColor = Color.LightGray)

                    QuestionBoardingScreen(
                        quiz = boardingState.quiz,
                        exactAnswer = boardingState.answer,
                        onAction = viewModel::onBoardingActionPressed,
                        onAnswer = { chosenAnswer ->
                            boardingState.apply {
                                answer = chosenAnswer
                                enableNext = true
                                isCorrect = when(quiz.answer) {
                                    is Quiz.Answer.Possible.SingleChoice -> {
                                        val singleChoice = quiz.answer as Quiz.Answer.Possible.SingleChoice

                                        chosenAnswer.answer == singleChoice.answer
                                    }
                                    is Quiz.Answer.Possible.MultipleChoice -> {
                                        val multipleChoice = quiz.answer as Quiz.Answer.Possible.MultipleChoice

                                        chosenAnswer.answers == multipleChoice.answers
                                    }
                                    else -> false
                                }
                            }
                        }
                    )
                }
            }
        )
    }
}
