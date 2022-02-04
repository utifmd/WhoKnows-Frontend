package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Title
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.model.Room
import com.dudegenuine.whoknows.ui.compose.component.GeneralTextField
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ui.compose.state.RoomState
import com.dudegenuine.whoknows.ui.presenter.ResourceState
import com.dudegenuine.whoknows.ui.presenter.room.RoomViewModel

/**
 * Tue, 11 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun RoomCreatorScreen(
    modifier: Modifier = Modifier,
    viewModel: RoomViewModel = hiltViewModel(),
    onSucceed: (Room) -> Unit){

    val state = viewModel.state
    val formState = viewModel.formState

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            GeneralTopBar(
                title = "New class",
                submitLabel = "Create",
                submitEnable = formState.isPostValid && !state.loading,
                submitLoading = state.loading,
                onSubmitPressed = { viewModel.onCreatePressed(onSucceed) }
            )
        },
        content = {
            Body(
                resourceState = state,
                formState = formState,
            )
        }
    )
}

@Composable
private fun Body(
    resourceState: ResourceState,
    formState: RoomState.FormState,
    modifier: Modifier = Modifier){

    val isExpand = remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(12.dp)) {

        GeneralTextField(
            label = "Enter title",
            value = formState.title.text,
            onValueChange = formState::onTitleChange,
            leads = Icons.Default.Title,
            tails = if (formState.title.text.isNotBlank()) Icons.Default.Close else null,
            onTailPressed = { formState.onTitleChange("") }
        )

        GeneralTextField(
            label = "Enter description",
            value = formState.desc.text,
            singleLine = false,
            leads = Icons.Default.Description,
            onValueChange = formState::onDescChange,
            tails = if (formState.desc.text.isNotBlank()) Icons.Default.Close else null,
            onTailPressed = { formState.onDescChange("") }
        )

        GeneralTextField(
            label = "Select class duration",
            value = formState.minute.text,
            leads = Icons.Default.Timer,
            onValueChange = formState::onMinuteChange,
            tails = if (formState.minute.text.isNotBlank()) "minute\'s" else "minute",
            onTailPressed = {
                isExpand.value = !isExpand.value
            },
            isExpand = isExpand.value,
            onExpanderSelected = {
                formState.onMinuteChange(it.toString())
                isExpand.value = false
            },
            onExpanderDismiss = {
                isExpand.value = false
            },
            readOnly = true
        )

        if (resourceState.error.isNotBlank()){
            ErrorScreen(message = resourceState.error, isSnack = true)
        }
    }
}