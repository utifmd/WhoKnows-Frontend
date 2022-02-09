package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.model.PossibleAnswer
import com.dudegenuine.model.QuizActionType
import com.dudegenuine.whoknows.ui.compose.component.misc.FieldTag
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.QuestionBoardingScreen
import com.dudegenuine.whoknows.ui.compose.state.RoomState

/**
 * Fri, 17 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
@ExperimentalCoilApi
@ExperimentalMaterialApi
fun RoomBoardingScreen(
    // resourceState: ResourceState,
    modifier: Modifier = Modifier,
    state: RoomState.BoardingQuiz,
    onAction: (Int, QuizActionType) -> Unit,
    onBackPressed: () -> Unit,
    onPrevPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onDonePressed: () -> Unit) {

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed) // val selectedMenu = remember { mutableStateOf("") }
    val boardingState = remember(state.currentQuestionIdx){
        state.list[state.currentQuestionIdx]
    }

    BackdropScaffold(
        scaffoldState = scaffoldState,
        appBar = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.fillMaxWidth()) { // .padding(16.dp)
                if (boardingState.showPrevious){
                    TextButton( //val question = Quiz("QIZ-${UUID.randomUUID()}", "ROM-f80365e5-0e65-4674-9e7b-bee666b62bda", images = listOf("https://avatars.githubusercontent.com/u/16291551?s=400&u=c0b02c25fef325be78f7a1faca541f44120fb591&v=4"), "Bagaimana ciri fisik pria tersebut?", options = listOf("Ikal", "Sawo matang", "Misterius", "Tinggi", "Kumis tipis", "Kurus kering"), answer = PossibleAnswer.MultipleChoice(setOf("Ikal", "Sawo matang", "Misterius", "Kumis tipis")), createdBy = "Utif Milkedori", createdAt = Date(), null) //                            quizViewModel.postQuiz(question)
                        onClick = onPrevPressed) {
                        Text(text = "Previous", color = MaterialTheme.colors.onPrimary)
                    }
                }
                Text(
                    modifier = modifier.padding(
                        horizontal = 16.dp
                    ),
                    text = "${state.room.minute} time left",
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h6
                )
                Row {
                    if (boardingState.showDone) {
                        TextButton(
                            enabled = boardingState.enableNext,
                            onClick = onDonePressed) {
                            Text(text = "Done", color = MaterialTheme.colors.onPrimary)
                        }
                    } else {
                        TextButton(
                            enabled = boardingState.enableNext,
                            onClick = onNextPressed
                        ) {
                            Text(text = "Next", color = MaterialTheme.colors.onPrimary)
                        }
                    }
                }
            }
        },

        backLayerContent = {
            Column(
                modifier = modifier.padding(12.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally) {

                FieldTag(key = "Title", value = state.room.title, color = MaterialTheme.colors.onPrimary)
                FieldTag(key = "Description", value = state.room.description, color = MaterialTheme.colors.onPrimary)
                FieldTag(key = "Total duration", value = "${state.room.minute} minute\'s", color = MaterialTheme.colors.onPrimary)
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
                    onAction = onAction,
                    onAnswer = { chosenAnswer ->
                        boardingState.apply {
                            answer = chosenAnswer
                            enableNext = true
                            isCorrect = when(quiz.answer) {
                                is PossibleAnswer.SingleChoice -> {
                                    val singleChoice = quiz.answer as PossibleAnswer.SingleChoice

                                    chosenAnswer.answer == singleChoice.answer
                                }
                                is PossibleAnswer.MultipleChoice -> {
                                    val multipleChoice = quiz.answer as PossibleAnswer.MultipleChoice

                                    chosenAnswer.answers == multipleChoice.answers
                                }
                                else -> return@apply
                            }
                        }
                    }
                )
            }
        }
    )
}
