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
import com.dudegenuine.whoknows.ui.compose.state.RoomState
import com.dudegenuine.whoknows.ui.presenter.room.RoomViewModel
import kotlinx.coroutines.launch

/**
 * Fri, 17 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
@ExperimentalMaterialApi
fun RoomBoardingScreen(
    /*
    * Fragment stuff
    * */
    roomViewModel: RoomViewModel = hiltViewModel(),

    /*
    * Compose stuff
    * */
    stateBoardingQuiz: RoomState.BoardingQuiz,
    onAction: () -> Unit,
    onDonePressed: () -> Unit,
    onBackPressed: () -> Unit) {

    /*
    * Fragment stuff
    * */
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    val selectedMenu = remember { mutableStateOf("") }

    val resourceState = roomViewModel.resourceState.value
    val uiState = roomViewModel.uiState.value

    /*
    * Compose stuff
    * */
    val questionIndex = remember { mutableStateOf(0) }
    val totalQuestionsCount = resourceState.questions?.size ?: 0

    BackdropScaffold(
        scaffoldState = scaffoldState,
        appBar = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()) { // .padding(16.dp)
                TextButton(
                    onClick = {
                        scope.launch {
//                            val question = Quiz("QIZ-${UUID.randomUUID()}", "ROM-f80365e5-0e65-4674-9e7b-bee666b62bda", images = listOf("https://avatars.githubusercontent.com/u/16291551?s=400&u=c0b02c25fef325be78f7a1faca541f44120fb591&v=4"), "Bagaimana ciri fisik pria tersebut?", options = listOf("Ikal", "Sawo matang", "Misterius", "Tinggi", "Kumis tipis", "Kurus kering"), answer = PossibleAnswer.MultipleChoice(setOf("Ikal", "Sawo matang", "Misterius", "Kumis tipis")), createdBy = "Utif Milkedori", createdAt = Date(), null)
//                            quizViewModel.postQuiz(question)
                        }
                    }) {
                    Text(
                        text = "Skip",
                        color = Color.LightGray )}
            if(resourceState.loading) {
                CircularProgressIndicator(color = MaterialTheme.colors.surface)}
                Row(
                    verticalAlignment = Alignment.CenterVertically) {
                    TextButton(
                        enabled = !resourceState.loading,
                        onClick = {
                            scope.launch {
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
                    progress = questionIndex.value +1 / totalQuestionsCount.toFloat(),
                    modifier = Modifier
                        .requiredWidth(126.dp)
                        .padding(16.dp),
                    color = MaterialTheme.colors.primaryVariant,
                    backgroundColor = Color.LightGray)
                Text(
                    text = resourceState.questions?.let { it[questionIndex.value].question } ?: "loading...") //selectedMenu.value
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