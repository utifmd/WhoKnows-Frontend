package com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.model.PossibleAnswer
import com.dudegenuine.model.common.ImageUtil.strOf
import com.dudegenuine.whoknows.ui.compose.component.GeneralButtonGroup
import com.dudegenuine.whoknows.ui.compose.component.GeneralTextField
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.presenter.quiz.QuizViewModel

/**
 * Tue, 28 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
@ExperimentalFoundationApi
fun QuizCreatorScreen(
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val formState = viewModel.formState

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
                title = "New question",
                submitLabel = "Post",
                submitEnable = formState.isValid.value,
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
                modifier = modifier.fillMaxSize()) {
                item {
                    ImagesPreUpload(
                        images = formState.images,
                        onAddPressed = { launcher.launch("image/*") },
                        onRemovePressed = formState::onImagesRemoveAt
                    )
                }
                stickyHeader {
                    GeneralTextField(
                        label = "Enter a question",
                        value = formState.currentQuestion.text,
                        onValueChange = formState::onQuestionValueChange,
                        tails = if (formState.currentQuestion.text.isNotBlank())
                                Icons.Default.Close else null,
                        onTailPressed = { formState.onQuestionValueChange("") },
                        modifier = modifier.background(
                            color = MaterialTheme.colors.surface
                        )
                    )
                }
                item {
                    GeneralTextField(
                        label = "Push some options",
                        value = formState.currentOption.text,
                        onValueChange = formState::onOptionValueChange,
                        tails = if (formState.currentOption.text.isNotBlank())
                            Icons.Default.AddCircleOutline else null,
                        onTailPressed = formState::onPushedOption,
                        modifier = Modifier
                            .onKeyEvent(formState::onOptionKeyEvent)
                    )
                }
                item {
                    Spacer(modifier = modifier.height(8.dp))
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
                            answer = formState.currentAnswer,
                            onAnswerSelected = formState::onAnsweredSingle
                        )
                    }
                    strOf<PossibleAnswer.MultipleChoice>() -> item {
                        MultipleChoiceQuestion(
                            options = formState.options,
                            answer = formState.currentAnswer,
                            onAnswerSelected = formState::onAnsweredMultiple
                        )
                    }
                }
            }
        }
    )
}