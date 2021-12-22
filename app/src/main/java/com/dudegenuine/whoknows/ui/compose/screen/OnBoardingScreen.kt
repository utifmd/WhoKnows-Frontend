package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.model.PossibleAnswer
import com.dudegenuine.model.Quiz
import com.dudegenuine.whoknows.ui.presenter.quiz.QuizViewModel
import kotlinx.coroutines.launch
import java.util.*

/**
 * Fri, 17 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
@ExperimentalMaterialApi
fun OnBoardingScreen(
    viewModel: QuizViewModel = hiltViewModel()) {

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    val selectedMenu = remember { mutableStateOf("") }
    val questionIndex = remember { mutableStateOf(0) }
    val totalQuestionsCount = 7

    BackdropScaffold(
        scaffoldState = scaffoldState,
        appBar = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()) { // .padding(16.dp)
                TextButton(
                    onClick = {}) {
                    Text(
                        text = "Skip",
                        color = Color.LightGray )}
                Row(
                    verticalAlignment = Alignment.CenterVertically) {
                    TextButton(
                        onClick = {
                            scope.launch {
//                                val user = User("USR-${UUID.randomUUID()}", "Diyanti Ratna Puspita Sari", "dian@gmail.com", "081275340004", "dian", "12121212", Date(), null)
//                                val room = Room("ROM-${UUID.randomUUID()}", "USR-ff3cbd05-f4e6-4949-b170-aa78e57dde81", 60, "Accounting Elementary", "Select the correct answer", false, Date(), null, null, null)
                                val question = Quiz("QIZ-${UUID.randomUUID()}", "ROM-f80365e5-0e65-4674-9e7b-bee666b62bda", images = listOf("https://avatars.githubusercontent.com/u/16291551?s=400&u=c0b02c25fef325be78f7a1faca541f44120fb591&v=4", "https://avatars.githubusercontent.com/u/16291551?s=400&u=c0b02c25fef325be78f7a1faca541f44120fb591&v=4"), "Tahun berapakah pria tersebut tampil di dunia?", options = listOf("Tahun 2000", "Tahun 1976", "Tahun 1999", "Tahun 1994"), answer = PossibleAnswer.SingleChoice("Tahun 1994"), createdBy = "Utif Milkedori", createdAt = Date(), null)
                                viewModel.postQuiz(question)
                                questionIndex.value += 1
                            }}) {
                        Icon(
                            tint = Color.LightGray,
                            imageVector = Icons.Default.Check,
                            contentDescription = "next")
                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                        Text(text = "Next", color = Color.LightGray )}}}},
        backLayerContent = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally) {
                listOf("Menu fruits", "Menu foods", "Menu drinks", "Menu clothes", "Menu outfit", "Menu gadgets").forEach {
                    Text(
                        text = it,
                        fontSize = 24.sp,
                        modifier = Modifier.clickable {
                            scope.launch {
                                selectedMenu.value = it
                                scaffoldState.conceal() }}) }}},
        frontLayerContent = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween) {
                LinearProgressIndicator(
                    progress = questionIndex.value / totalQuestionsCount.toFloat(),
                    modifier = Modifier
                        .requiredWidth(126.dp)
                        .padding(16.dp),
                    color = MaterialTheme.colors.primaryVariant,
                    backgroundColor = Color.LightGray)
                Text(
                    text = selectedMenu.value)
                Spacer(Modifier.height(64.dp))
                TextButton(
                    onClick = {
                        scope.launch {
                            scaffoldState.reveal() }}) {
                    Text(text = "Select a different one?")
                }
            }
        }
    )
}

//@Preview
//@Composable
//fun OnBoardingScreen() {
//
//    Box {
//        BackgroundStack {
//            Column(
//                verticalArrangement = Arrangement.Bottom,
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier.offset(y = (-30).dp)) {
//                Row {
//                    for (i in 1..5){
//                        Box(modifier = Modifier
//                            .size(10.dp)
//                            .background(
//                                color = Color.LightGray,
//                                shape = CircleShape
//                            ))
//                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
//                    }
//                }
//                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)) {
//                    TextButton(onClick = {}) {
//                        Text(text = "Skip", color = Color.White)
//                    }
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        TextButton(onClick = {}) {
//                            Text(text = "Next", color = Color.White)
//                            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
//                            Icon(
//                                tint = Color.Blue,
//                                imageVector = Icons.Default.ArrowForwardIos,
//                                contentDescription = "next"
//                            )
//                        }
//                    }
//                }
//            }
//        }
//        ForegroundTopStack {
//            Column(
//                modifier = Modifier.padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally) {
//                Image(
//                    imageVector = Icons.Default.CleaningServices,
//                    modifier = Modifier.fillMaxWidth(),
//                    contentDescription = "CleaningServices")
//                Spacer(
//                    modifier = Modifier.padding(32.dp))
//                Text(
//                    text = "Cleaning on Demand",
//                    style = MaterialTheme.typography.h6) // CompositionLocalProvider { }
//                Spacer(
//                    modifier = Modifier.padding(vertical = 12.dp))
//                Text(
//                    text = "Book an appointment in less than 60 seconds and get on the schedule as early as tomorrow.",
//                    style = MaterialTheme.typography.caption,
//                    textAlign = TextAlign.Center)
//            }
//        }
//    }
//}