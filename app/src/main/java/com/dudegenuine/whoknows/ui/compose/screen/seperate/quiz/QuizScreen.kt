package com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.model.Answer
import com.dudegenuine.model.PossibleAnswer
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.common.ImageUtil
import com.dudegenuine.whoknows.ui.compose.component.GeneralCardView
import com.dudegenuine.whoknows.ui.compose.component.GeneralImage
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.compose.component.misc.FieldTag
import com.dudegenuine.whoknows.ui.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ui.compose.screen.LoadingScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.contract.IQuizPrivateState
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.contract.IQuizPublicState
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.contract.IQuizState
import com.dudegenuine.whoknows.ui.vm.quiz.QuizViewModel
import okhttp3.internal.http.toHttpDateString

/**
 * Mon, 31 Jan 2022
 * WhoKnows by utifmd
 **/
@ExperimentalCoilApi
@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    stateCompose: IQuizState) {

    when(stateCompose){
        is IQuizPrivateState -> PrivateBody(
            modifier = modifier,
            contentModifier = contentModifier,
            model = stateCompose.model,
            answer = stateCompose.answer,
            stateCompose = stateCompose
        )

        is IQuizPublicState -> PublicBody(
            modifier = modifier,
            contentModifier = contentModifier,
            stateCompose = stateCompose,
            onBackPressed = stateCompose
                .event::onBackPressed
        )
        else -> ErrorScreen(message = "State compose does\'nt exist")
    }
}

@ExperimentalCoilApi
@Composable
private fun Body(
    modifier: Modifier = Modifier,
    model: Quiz,
    onPicturePressed: (String) -> Unit,
    content: @Composable () -> Unit){

    val backgroundColor = MaterialTheme.colors.onSurface.copy(
        alpha = if(MaterialTheme.colors.isLight) 0.04f else 0.06f)

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)) {

        Spacer(
            modifier = modifier.height(36.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier.fillMaxWidth()){

            model.images.map { url ->
                GeneralImage(
                    modifier = modifier
                        .size(240.dp)
                        .clickable(
                            onClick = { onPicturePressed(url) }),
                    data = if(url.contains("://")) url else ImageUtil.asBitmap(url),
                    placeholder = {
                        Icon(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            imageVector = Icons.Default.Person, tint = MaterialTheme.colors.primary,
                            contentDescription = null
                        )
                    }
                )
            }
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = backgroundColor,
                    shape = MaterialTheme.shapes.small
                )) {
            Text(
                text = model.question,
                style = MaterialTheme.typography.subtitle1,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 24.dp,
                        horizontal = 16.dp
                    )
            )
        }
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 8.dp
                    ),
                text = "Posted at ${model.createdAt.toHttpDateString()}",
                style = MaterialTheme.typography.caption,
            )
        }

        GeneralCardView { content() }
    }
}

@ExperimentalCoilApi
@Composable
private fun PrivateBody (
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    model: Quiz,
    answer: Answer?,
    stateCompose: IQuizPrivateState,
    scrollState: ScrollState = rememberScrollState()) {

    Column(
        modifier = contentModifier
            .fillMaxSize()
            .verticalScroll(scrollState)) {

        Body(modifier, model, stateCompose.event::onPicturePressed){
            when(model.answer) {
                is PossibleAnswer.SingleChoice -> SingleChoiceQuestion(
                    options = model.options,
                    answer = answer,
                    onAnswerSelected = stateCompose.event::onAnswerSelected
                )
                is PossibleAnswer.MultipleChoice -> MultipleChoiceQuestion(
                    options = model.options,
                    answer = answer,
                    onAnswerSelected = stateCompose.event::onAnswerSelected
                )
                else -> ErrorScreen(
                    message = "Exactly answer does\'nt exist"
                )
            }
        }

    }
}

@ExperimentalCoilApi
@Composable
private fun PublicBody (
    modifier: Modifier = Modifier,
    contentModifier: Modifier,
    stateCompose: IQuizPublicState,
    viewModel: QuizViewModel = hiltViewModel(),
    scrollState: ScrollState = rememberScrollState(), onBackPressed: () -> Unit){
    val state = viewModel.state

    Scaffold(modifier,
        topBar = { GeneralTopBar(
            title = "Question detail",
            leads = Icons.Filled.ArrowBack,
            onLeadsPressed = onBackPressed) }) {

        if (state.loading)
            LoadingScreen()

        state.quiz?.let { model ->
            Column(
                modifier = contentModifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)) {

                Body(modifier, model, stateCompose.event::onPicturePressed){
                    when(model.answer){
                        is PossibleAnswer.SingleChoice ->
                            FieldTag(
                                key = "Answer",
                                editable = false,
                                value = (model.answer as PossibleAnswer.SingleChoice).answer)
                        is PossibleAnswer.MultipleChoice ->
                            FieldTag(
                                key = "Answers",
                                editable = false,
                                value = (model.answer as PossibleAnswer.MultipleChoice)
                                    .answers.joinToString(", "))
                        else -> return@Body
                    }
                }
            }
        }

        if (state.error.isNotBlank())
            ErrorScreen(message = state.error)
    }
}