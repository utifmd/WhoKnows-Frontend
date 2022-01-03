package com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz

import android.graphics.Bitmap
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.model.PossibleAnswer
import com.dudegenuine.model.common.Utility.strOf
import com.dudegenuine.whoknows.R
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
            TopBar(
                enabled = viewModel.isValid.value,
                onPostPressed = viewModel::onPostPressed
            )
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
                    ImageRows(
                        images = viewModel.images,
                        onAddPressed = {
                            launcher.launch("image/*")
                        },
                        onRemovePressed = {
                            viewModel.images.removeAt(it)
                        }
                    )
                }
                stickyHeader {
                    TextField(
                        value = viewModel.currentQuestion.value,
                        onValueChange = {
                            viewModel.currentQuestion.value = it
                        },
                        label = { Text(
                            text = "Enter a question"
                        )},
                        trailingIcon = {
                           if (viewModel.currentQuestion.value.isNotBlank()){
                               Icon(
                                   imageVector = Icons.Default.Close,
                                   contentDescription = "deleteQuestionText",
                                   modifier = Modifier.clickable {
                                       viewModel.currentQuestion.value = ""
                                   }
                               )
                           }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colors.surface
                            )
                    )
                }
                item {
                    TextField(
                        value = viewModel.currentOption.value,
                        onValueChange = { viewModel.currentOption.value = it },
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
                            viewModel.selectedAnswer.value = null
                        }
                    )
                }
                when(selectedType.value){
                    strOf<PossibleAnswer.SingleChoice>() -> item {
                        SingleChoiceQuestion(
                            options = viewModel.options,
                            answer = viewModel.currentAnswer.value,
                            onAnswerSelected = viewModel.onAnsweredSingle
                        )
                    }
                    strOf<PossibleAnswer.MultipleChoice>() -> item {
                        MultipleChoiceQuestion(
                            options = viewModel.options,
                            answer = viewModel.currentAnswer.value,
                            onAnswerSelected = viewModel.onAnsweredMultiple
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun ImageRows(
    images: List<Bitmap>,
    onAddPressed: () -> Unit,
    onRemovePressed: (Int) -> Unit) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)){
        itemsIndexed(images) { idx, bitmap ->
            Box(
                contentAlignment = Alignment.TopEnd) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "uri $idx",
                    modifier = Modifier.size(128.dp))
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "iconClose",
                    tint = MaterialTheme.colors.surface,
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .padding(4.dp)
                        .background(MaterialTheme.colors.primary)
                        .size(24.dp)
                        .clickable { onRemovePressed(idx) }
                )
            }
        }
        item {
            Icon(
                imageVector = Icons.Default.AddCircleOutline,
                tint = MaterialTheme.colors.primary,
                contentDescription = "landscape",
                modifier = Modifier
                    .size(35.dp)
                    .padding(5.dp)
                    .clickable {
                        onAddPressed()
                    }
            )
        }
    }
}

@Composable
fun TopBar(enabled: Boolean, onPostPressed: () -> Unit){
    TopAppBar {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.app_name),
                modifier = Modifier.padding(
                    horizontal = 8.dp
                )
            )

            TextButton(
                enabled = enabled,
                onClick = onPostPressed) {
                Text(
                    text = "Post",
                    color = if (enabled) MaterialTheme.colors.surface
                        else MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}