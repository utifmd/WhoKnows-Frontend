package com.dudegenuine.whoknows.ux.compose.screen.seperate.room

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.dudegenuine.whoknows.ux.compose.component.GeneralTextField
import com.dudegenuine.whoknows.ux.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ux.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ux.compose.state.ResourceState
import com.dudegenuine.whoknows.ux.compose.state.RoomState
import com.dudegenuine.whoknows.ux.vm.room.RoomViewModel

/**
 * Tue, 11 Jan 2022
 * WhoKnows by utifmd
 **/
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RoomCreatorScreen(
    modifier: Modifier = Modifier,
    viewModel: RoomViewModel){

    val state = viewModel.state
    val formState = viewModel.roomState

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            GeneralTopBar(
                title = "New class",
                leads = Icons.Filled.ArrowBack,
                onLeadsPressed = viewModel::onBackPressed,
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

    //BackHandler(onBack = onBackPressed)
}

@Composable
private fun Body(
    resourceState: ResourceState,
    formState: RoomState){
    val focusManager = LocalFocusManager.current
    var isExpand by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(12.dp)) {

        GeneralTextField(
            label = "Enter title",
            value = formState.title.text,
            onValueChange = formState::onTitleChange,
            leads = Icons.Filled.Title,
            trail = if (formState.title.text.isNotBlank()) Icons.Filled.Close else null,
            onTailPressed = { formState.onTitleChange("") },
            keyboardActions = KeyboardActions(
                onDone = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        GeneralTextField(
            label = "Enter description",
            value = formState.desc.text,
            singleLine = false,
            leads = Icons.Filled.Description,
            onValueChange = formState::onDescChange,
            trail = if (formState.desc.text.isNotBlank()) Icons.Filled.Close else null,
            onTailPressed = { formState.onDescChange("") },
            keyboardActions = KeyboardActions(
                onDone = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        GeneralTextField(
            label = "Select class duration",
            value = formState.minute.text,
            leads = Icons.Filled.Timer,
            onValueChange = formState::onMinuteChange,
            trail = if (formState.minute.text.isNotBlank()) "minute\'s" else "minute",
            onTailPressed = { isExpand = !isExpand },
            isExpand = isExpand,
            onExpanderSelected = {
                formState.onMinuteChange(it.toString())
                isExpand = false
            },
            onExpanderDismiss = { isExpand = false },
            readOnly = true,
            keyboardActions = KeyboardActions(
                onDone = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        if (resourceState.error.isNotBlank()){
            ErrorScreen(message = resourceState.error, isSnack = true)
        }
    }
}