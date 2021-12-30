package com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.model.PossibleAnswer
import com.dudegenuine.model.common.Utility.strOf
import com.dudegenuine.whoknows.ui.compose.component.ButtonGroup
import com.dudegenuine.whoknows.ui.presenter.quiz.QuizViewModel

/**
 * Tue, 28 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
@ExperimentalFoundationApi
fun QuizCreatorScreen(
    viewModel: QuizViewModel = hiltViewModel()) {
    val TAG = "QuizCreatorScreen"
    val context = LocalContext.current

    val selectedType = remember {
        mutableStateOf(strOf<PossibleAnswer.SingleChoice>())
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            viewModel.onResultImage(context, it)
        }
    )

    Scaffold(
        topBar = {
            TopAppBar {
                TextButton(onClick = viewModel.onPostPressed) {
                    Text(text = "Post", color = MaterialTheme.colors.surface)
                }
            }
        },

        content = {
            LazyColumn(
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)){
                        itemsIndexed(viewModel.images) { idx, bitmap ->
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "uri $idx",
                                modifier = Modifier.size(128.dp))
                        }
                        item {
                            Icon(
                                imageVector = Icons.Default.AddAPhoto,
                                tint = MaterialTheme.colors.primary,
                                contentDescription = "landscape",
                                modifier = Modifier
                                    .size(128.dp)
                                    .clickable {
                                        launcher.launch("image/*")
                                    }
                            )
                        }
                    }
                }
                stickyHeader {
                    TextField(
                        value = viewModel.question.value,
                        onValueChange = { viewModel.question.value = it },
                        label = { Text(
                            text = "Enter a question"
                        )},
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colors.surface
                            )
                    )
                }
                item {
                    TextField(
                        value = viewModel.mOption.value,
                        onValueChange = { viewModel.mOption.value = it },
                        singleLine = true,
                        label = { Text(
                            text = "Push some option"
                        )},
                        trailingIcon = { Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "AddOption",
                            modifier = Modifier.clickable(
                                onClick = viewModel.onPushedOption
                            )
                        )},
                        modifier = Modifier
                            .fillMaxWidth()
                            .onKeyEvent(viewModel.onOptionKeyEvent)
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    ButtonGroup(
                        buttons = setOf(
                            strOf<PossibleAnswer.SingleChoice>(),
                            strOf<PossibleAnswer.MultipleChoice>()
                        ),
                        value = selectedType.value,
                        onValueChange = {
                            selectedType.value = it
                        }
                    )
                }
                when(selectedType.value){
                    strOf<PossibleAnswer.SingleChoice>() -> item {
                        SingleChoiceQuestion(
                            options = viewModel.options,
                            answer = viewModel.mAnswer.value,
                            onAnswerSelected = viewModel.onAnsweredSingle
                        )
                    }
                    strOf<PossibleAnswer.MultipleChoice>() -> item {
                        MultipleChoiceQuestion(
                            options = viewModel.options,
                            answer = viewModel.mAnswer.value,
                            onAnswerSelected = viewModel.onAnsweredMultiple
                        )
                    }
                }
            }
        }
    )
}