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
import androidx.compose.ui.unit.sp
import com.dudegenuine.model.PossibleAnswer
import com.dudegenuine.model.QuizActionType
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.QuestionBoardingScreen
import com.dudegenuine.whoknows.ui.compose.state.RoomState
import kotlinx.coroutines.launch

/**
 * Fri, 17 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
@ExperimentalMaterialApi
fun RoomBoardingScreen(
    // resourceState: ResourceState,
    state: RoomState.BoardingQuiz,
    onAction: (Int, QuizActionType) -> Unit,
    // onBackPressed: () -> Unit,
    onPrevPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onDonePressed: () -> Unit) {
    // val TAG = "RoomBoardingScreen"

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
                modifier = Modifier.fillMaxWidth()) { // .padding(16.dp)
                if (boardingState.showPrevious){
                    TextButton( //val question = Quiz("QIZ-${UUID.randomUUID()}", "ROM-f80365e5-0e65-4674-9e7b-bee666b62bda", images = listOf("https://avatars.githubusercontent.com/u/16291551?s=400&u=c0b02c25fef325be78f7a1faca541f44120fb591&v=4"), "Bagaimana ciri fisik pria tersebut?", options = listOf("Ikal", "Sawo matang", "Misterius", "Tinggi", "Kumis tipis", "Kurus kering"), answer = PossibleAnswer.MultipleChoice(setOf("Ikal", "Sawo matang", "Misterius", "Kumis tipis")), createdBy = "Utif Milkedori", createdAt = Date(), null) //                            quizViewModel.postQuiz(question)
                        onClick = onPrevPressed) {
                        Text(text = "Previous", color = Color.LightGray)
                    }
                }
                Text(
                    text = "Question number ${boardingState.questionIndex} from ${boardingState.totalQuestionsCount}",
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Row {
                    if (boardingState.showDone) {
                        TextButton(
                            enabled = boardingState.enableNext,
                            onClick = onDonePressed) {
                            Text(text = "Done", color = Color.LightGray)
                        }
                    } else {
                        TextButton(
                            enabled = boardingState.enableNext,
                            onClick = onNextPressed
                        ) {
                            Text(text = "Next", color = Color.LightGray)
                        }
                    }
                }
            }
        },

        backLayerContent = {
            Column(
                modifier = Modifier.fillMaxSize(), // verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = state.room.title, fontSize = 24.sp)
                Text(text = state.room.description, fontSize = 16.sp)
                Text(text = "Total ${state.room.minute} minutes", fontSize = 14.sp)
                Spacer(modifier = Modifier.height(24.dp))
            }
       },

        frontLayerContent = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween) {
                LinearProgressIndicator(
                    progress = boardingState.questionIndex / boardingState.totalQuestionsCount.toFloat(),
                    modifier = Modifier.requiredWidth(126.dp).padding(16.dp),
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
                    })
                TextButton(
                    onClick = {
                        scope.launch {
                            scaffoldState.reveal()
                        }}) {
                    Text(text = "Room detail description")
                }
            }
        }
    )
}
