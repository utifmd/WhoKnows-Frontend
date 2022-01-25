package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.layout.*
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
    viewModel: RoomViewModel = hiltViewModel()){

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
                onSubmitPressed = viewModel::onCreatePressed
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
        modifier = Modifier
            .padding(16.dp)) {

        GeneralTextField(
            label = "Enter title",
            value = formState.title.text,
            onValueChange = formState::onTitleChange,
            leads = Icons.Default.Title,
            tails = if (formState.title.text.isNotBlank()) Icons.Default.Close else null,
            onTailPressed = { formState.onTitleChange("") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        GeneralTextField(
            label = "Enter description",
            value = formState.desc.text,
            singleLine = false,
            leads = Icons.Default.Description,
            onValueChange = formState::onDescChange,
            tails = if (formState.desc.text.isNotBlank()) Icons.Default.Close else null,
            onTailPressed = { formState.onDescChange("") }
        )
        Spacer(
            modifier = Modifier.height(8.dp))

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
            Spacer(modifier = modifier.height(16.dp))
            ErrorScreen(message = resourceState.error, isSnack = true)
        }
    }
}