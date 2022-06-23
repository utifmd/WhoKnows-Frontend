package com.dudegenuine.whoknows.ux.compose.screen.seperate.quiz

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.common.ImageUtil.strOf
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ux.compose.component.GeneralButtonGroup
import com.dudegenuine.whoknows.ux.compose.component.GeneralTextField
import com.dudegenuine.whoknows.ux.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ux.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ux.vm.quiz.QuizViewModel

/**
 * Tue, 28 Dec 2021
 * WhoKnows by utifmd
 **/
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun QuizCreatorScreen(
    modifier: Modifier = Modifier, viewModel: QuizViewModel/*, onBackPressed: () -> Unit, onSucceed: (Quiz.Complete) -> Unit*/) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val scrollState = rememberScrollState()
    val selectedType = remember{ mutableStateOf(strOf<Quiz.Answer.Possible.SingleChoice>()) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()){
        viewModel.quizState.onResultImage(context, it)
    }
    Scaffold(modifier,
        topBar = {
            GeneralTopBar(
                title = stringResource(R.string.new_question),
                leads = Icons.Filled.ArrowBack,
                onLeadsPressed = viewModel::onBackPressed,
                submitLabel = "Add",
                submitEnable = viewModel.quizState.isValid,
                submitLoading = viewModel.state.loading,
                onSubmitPressed = viewModel::onPostPressed)
        },
        content = {
            Box(modifier.verticalScroll(scrollState)) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = modifier
                        .fillMaxSize()
                        .padding(12.dp)) {

                    ImagesPreUpload(
                        images = viewModel.quizState.images,
                        onAddPressed = { launcher.launch("image/*") },
                        onRemovePressed = viewModel.quizState::onImagesRemoveAt
                    )
                    GeneralTextField(modifier,
                        label = "Enter a question",
                        value = viewModel.quizState.currentQuestion.text,
                        onValueChange = viewModel.quizState::onQuestionValueChange,
                        leads = Icons.Filled.QuestionAnswer,
                        trail = if (viewModel.quizState.currentQuestion.text.isNotBlank())
                                Icons.Filled.Close else null,
                        onTailPressed = { viewModel.quizState.onQuestionValueChange("") },
                        keyboardActions = KeyboardActions{ focusManager.moveFocus(FocusDirection.Down) }
                    )
                    GeneralTextField(
                        label = "Push some options",
                        value = viewModel.quizState.currentOption.text,
                        onValueChange = viewModel.quizState::onOptionValueChange,
                        leads = Icons.Filled.List,
                        trail = if (viewModel.quizState.currentOption.text.isNotBlank())
                            Icons.Filled.AddCircleOutline else null,
                        onTailPressed = viewModel.quizState::onPushedOption,
                        modifier = modifier.onKeyEvent(viewModel.quizState::onOptionKeyEvent),
                        keyboardActions = KeyboardActions{ keyboardController?.hide() }
                    )
                    GeneralButtonGroup(
                        buttons = setOf(
                            strOf<Quiz.Answer.Possible.SingleChoice>(),
                            strOf<Quiz.Answer.Possible.MultipleChoice>()
                        ),
                        value = selectedType.value,
                        onValueChange = {
                            selectedType.value = it
                            viewModel.quizState.onSelectedAnswerValue(null)
                        }
                    )

                    if (viewModel.state.error.isNotBlank()){
                        ErrorScreen(
                            message = viewModel.state.error, isSnack = true)
                    }

                    when(selectedType.value){
                        strOf<Quiz.Answer.Possible.SingleChoice>() ->
                            SingleChoiceQuestion(
                                options = viewModel.quizState.options,
                                answer = viewModel.quizState.currentAnswer,
                                onAnswerSelected = viewModel.quizState::onAnsweredSingle
                            )

                        strOf<Quiz.Answer.Possible.MultipleChoice>() ->
                            MultipleChoiceQuestion(
                                options = viewModel.quizState.options,
                                answer = viewModel.quizState.currentAnswer,
                                onAnswerSelected = viewModel.quizState::onAnsweredMultiple
                            )
                    }
                }
            }
        }
    )
}