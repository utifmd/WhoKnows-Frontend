package com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.model.PossibleAnswer
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.common.ImageUtil.strOf
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.compose.component.GeneralButtonGroup
import com.dudegenuine.whoknows.ui.compose.component.GeneralTextField
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ui.vm.quiz.QuizViewModel

/**
 * Tue, 28 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
@ExperimentalFoundationApi
fun QuizCreatorScreen(
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    onSucceed: (Quiz) -> Unit) {
    val context = LocalContext.current
    val state = viewModel.state
    val formState = viewModel.formState

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { formState.onResultImage(context, it) }
    )

    val selectedType = remember {
        mutableStateOf(strOf<PossibleAnswer.SingleChoice>())
    }

    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier,
        topBar = {
            GeneralTopBar(
                title = stringResource(R.string.new_question),
                leads = Icons.Filled.ArrowBack,
                onLeadsPressed = onBackPressed,
                submitLabel = "Add",
                submitEnable = formState.isValid,
                submitLoading = state.loading,
                onSubmitPressed = { viewModel.onPostPressed(onSucceed) }
            )
        },

        content = {

            Box(
                modifier = modifier.verticalScroll(scrollState)) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = modifier.fillMaxSize()
                        .padding(12.dp)) {

                    ImagesPreUpload(
                        images = formState.images,
                        onAddPressed = { launcher.launch("image/*") },
                        onRemovePressed = formState::onImagesRemoveAt
                    )

                    GeneralTextField(modifier,
                        label = "Enter a question",
                        value = formState.currentQuestion.text,
                        onValueChange = formState::onQuestionValueChange,
                        leads = Icons.Filled.QuestionAnswer,
                        tails = if (formState.currentQuestion.text.isNotBlank())
                                Icons.Filled.Close else null,
                        onTailPressed = { formState.onQuestionValueChange("") },
                    )

                    GeneralTextField(
                        label = "Push some options",
                        value = formState.currentOption.text,
                        onValueChange = formState::onOptionValueChange,
                        leads = Icons.Filled.List,
                        tails = if (formState.currentOption.text.isNotBlank())
                            Icons.Filled.AddCircleOutline else null,
                        onTailPressed = formState::onPushedOption,
                        modifier = Modifier
                            .onKeyEvent(formState::onOptionKeyEvent)
                    )

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

                    if (state.error.isNotBlank()){
                        ErrorScreen(
                            message = state.error, isSnack = true)
                    }

                    when(selectedType.value){
                        strOf<PossibleAnswer.SingleChoice>() ->
                            SingleChoiceQuestion(
                                options = formState.options,
                                answer = formState.currentAnswer,
                                onAnswerSelected = formState::onAnsweredSingle
                            )

                        strOf<PossibleAnswer.MultipleChoice>() ->
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