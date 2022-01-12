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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.model.PossibleAnswer
import com.dudegenuine.model.common.ImageUtil.asBitmap
import com.dudegenuine.model.common.ImageUtil.strOf
import com.dudegenuine.whoknows.ui.compose.component.GeneralButtonGroup
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.presenter.quiz.QuizViewModel

/**
 * Tue, 28 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
@ExperimentalFoundationApi
fun QuizCreatorScreen(
    viewModel: QuizViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val formState = viewModel.formState.value

    val selectedType = remember {
        mutableStateOf(strOf<PossibleAnswer.SingleChoice>())
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            formState.onResultImage(context, it)
        }
    )

    Scaffold(
        topBar = {
            GeneralTopBar(
                title = "Question",
                submission = "Post",
                isSubmit = formState.isValid.value,
                onSubmitPressed = viewModel::onPostPressed
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
                        images = formState.images,
                        onAddPressed = {
                            launcher.launch("image/*")
                        },
                        onRemovePressed = {
                            formState.images.removeAt(it)
                        }
                    )
                }
                stickyHeader {
                    TextField(
                        value = formState.currentQuestion.value,
                        onValueChange = formState.onQuestionValueChange,
                        label = {
                            Text(
                                text = "Enter a question"
                            )
                        },
                        trailingIcon = {
                           if (formState.currentQuestion.value.isNotBlank()){
                               Icon(
                                   imageVector = Icons.Default.Close,
                                   contentDescription = "deleteQuestionText",
                                   modifier = Modifier.clickable {
                                       formState.onQuestionValueChange("")
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
                        value = formState.currentOption.value,
                        onValueChange = formState.onOptionValueChange,
                        singleLine = true,
                        label = { Text(
                            text = "Push some option"
                        )},
                        trailingIcon = { Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "AddOption",
                            modifier = Modifier.clickable(
                                onClick = formState.onPushedOption
                            )
                        )},
                        modifier = Modifier
                            .fillMaxWidth()
                            .onKeyEvent(formState.onOptionKeyEvent)
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    GeneralButtonGroup(
                        buttons = setOf(
                            strOf<PossibleAnswer.SingleChoice>(),
                            strOf<PossibleAnswer.MultipleChoice>()
                        ),
                        value = selectedType.value,
                        onValueChange = {
                            selectedType.value = it
                            formState.onSelectedAnswerValue(null)
                        }
                    )
                }
                when(selectedType.value){
                    strOf<PossibleAnswer.SingleChoice>() -> item {
                        SingleChoiceQuestion(
                            options = formState.options,
                            answer = formState.currentAnswer.value,
                            onAnswerSelected = formState.onAnsweredSingle
                        )
                    }
                    strOf<PossibleAnswer.MultipleChoice>() -> item {
                        MultipleChoiceQuestion(
                            options = formState.options,
                            answer = formState.currentAnswer.value,
                            onAnswerSelected = formState.onAnsweredMultiple
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun ImageRows(
    images: List<ByteArray>,
    onAddPressed: () -> Unit,
    onRemovePressed: (Int) -> Unit) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)){
        itemsIndexed(images) { idx, byteArray ->
            Box(
                contentAlignment = Alignment.TopEnd) {

                Image( //painter = rememberImagePainter(data = uri),
                    bitmap = asBitmap(byteArray).asImageBitmap(),
                    contentDescription = "uri $idx",
                    modifier = Modifier.size(128.dp)
                )

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
                    .clickable(
                        enabled = images.size < 4,
                        onClick = onAddPressed
                    )
            )
        }
    }
}