package com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz

import android.graphics.Bitmap
import android.util.Log
import android.view.KeyEvent.KEYCODE_ENTER
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.model.Answer
import com.dudegenuine.model.PossibleAnswer
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.common.Utility.asBase64
import com.dudegenuine.model.common.Utility.strOf
import com.dudegenuine.whoknows.ui.compose.component.ButtonGroup
import com.dudegenuine.whoknows.ui.presenter.quiz.QuizViewModel
import java.util.*

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

    val question = remember { mutableStateOf("") }
    val option = remember { mutableStateOf("") }
    val options = remember { mutableStateListOf<String>() }
    val images = remember { mutableStateListOf<Bitmap>() }
    val currentAnswer = remember { mutableStateOf<Answer?>(null) }
    val selectedAnswer = remember { mutableStateOf<PossibleAnswer?>(null) }
    val selectedType = remember { mutableStateOf(strOf<PossibleAnswer.SingleChoice>()) }
    val multipleAnswer = remember { mutableSetOf<String>() }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { viewModel.onResultImage(context, it) }
    )

    val onPushedOption: () -> Unit = {
        options.add(option.value).apply {
            option.value = ""
        }
    }

    val onOptionKeyEvent: (KeyEvent) -> Boolean = {
        if (it.nativeKeyEvent.keyCode == KEYCODE_ENTER && option.value.isNotBlank())
            onPushedOption()
        false
    }

    val onPostPressed: () -> Unit = {
        val model = Quiz(
            "QIZ-${UUID.randomUUID()}",
            "ROM-f80365e5-0e65-4674-9e7b-bee666b62bda",
            images = images.map { asBase64(it) },
            question = question.value,
            options = options.toList(),
            answer = selectedAnswer.value,
            createdBy = "Diyanti Ratna Puspita Sari",
            createdAt = Date(),
            updatedAt = null
        )

        Log.d(TAG, model.toString())
    }

    Scaffold(
        topBar = {
            TopAppBar {
                TextButton(onClick = onPostPressed) {
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
                        itemsIndexed(images) { idx, bitmap ->
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
                        value = question.value,
                        onValueChange = { question.value = it },
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
                        value = option.value,
                        onValueChange = { option.value = it },
                        singleLine = true,
                        label = { Text(
                            text = "Push some option"
                        )},
                        trailingIcon = { Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "AddOption",
                            modifier = Modifier.clickable(
                                onClick = onPushedOption
                            )
                        )},
                        modifier = Modifier
                            .fillMaxWidth()
                            .onKeyEvent(onOptionKeyEvent)
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
                            options = options,
                            answer = currentAnswer.value,
                            onAnswerSelected = { newAnswer ->
                                selectedAnswer.value = PossibleAnswer.SingleChoice(newAnswer)
                            }
                        )
                    }
                    strOf<PossibleAnswer.MultipleChoice>() -> item {
                        MultipleChoiceQuestion(
                            options = options,
                            answer = currentAnswer.value,
                            onAnswerSelected = { newAnswer, selected ->

                                if (selected) multipleAnswer.add(newAnswer)
                                else multipleAnswer.remove(newAnswer)

                                selectedAnswer.value = PossibleAnswer.MultipleChoice(multipleAnswer)
                            }
                        )
                    }
                }
            }
        }
    )
}