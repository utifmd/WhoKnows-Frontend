package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dudegenuine.whoknows.ui.compose.component.GeneralTextField
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.compose.state.RoomState
import com.dudegenuine.whoknows.ui.presenter.ResourceState
import com.dudegenuine.whoknows.ui.presenter.room.RoomViewModel

/**
 * Tue, 11 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun RoomCreatorScreen(
    viewModel: RoomViewModel){

    val resourceState = viewModel.resourceState.value
    val formState = viewModel.createState

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            GeneralTopBar(
                title = "New class",
                submission = "Create",
                isSubmit = formState.isValid.value && !resourceState.loading,
                isProgress = resourceState.loading,
                onSubmitPressed = viewModel::onCreatePressed
            )
        },
        content = {
            Body(
                resourceState = resourceState,
                formState = formState,
            )
        }
    )
}

@Composable
private fun Body(
    resourceState: ResourceState,
    formState: RoomState.CreateRoom,
    modifier: Modifier = Modifier){

    val isExpand = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)) {

        GeneralTextField(
            label = "Enter title",
            value = formState.title.text,
            onValueChange = formState::onTitleChange,
            tails = if (formState.title.text.isNotBlank()) Icons.Default.Close else null,
            onTailPressed = { formState.onTitleChange("") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        GeneralTextField(
            label = "Enter description",
            value = formState.desc.text,
            singleLine = false,
            onValueChange = formState::onDescChange,
            tails = if (formState.desc.text.isNotBlank()) Icons.Default.Close else null,
            onTailPressed = { formState.onDescChange("") }
        )
        Spacer(
            modifier = Modifier.height(8.dp))

        GeneralTextField(
            label = "Select class duration",
            value = formState.minute.text,
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
            Text(text = resourceState.error, color = MaterialTheme.colors.error)
        }
    }
}