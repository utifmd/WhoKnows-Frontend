package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.InsertInvitation
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.compose.component.GeneralTextField
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ui.vm.room.RoomViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * Mon, 17 Jan 2022
 * WhoKnows by utifmd
 **/
@ExperimentalComposeUiApi
@FlowPreview
@ExperimentalCoroutinesApi
@Composable
fun RoomFinderScreen(
    modifier: Modifier = Modifier,
    viewModel: RoomViewModel = hiltViewModel(),
    onRoomSelected: (String) -> Unit, onBackPressed: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val formState = viewModel.formState

    Scaffold(modifier.fillMaxSize(),
        topBar = {
            GeneralTopBar(
                title = stringResource(
                    id = R.string.join_with_a_code
                ),
                leads = Icons.Filled.ArrowBack,
                onLeadsPressed = onBackPressed,
                submitLabel = "Search",
                submitEnable = formState.isGetValid,
                submitLoading = viewModel.state.loading,
                onSubmitPressed = { viewModel.getRoom(formState.roomId) }
            )
        },
        content = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier.padding(12.dp)) {

                GeneralTextField(
                    label = "Enter an invitation code",
                    value = formState.roomId,
                    leads = Icons.Filled.InsertInvitation,
                    tails = if(formState.roomId.isNotBlank()) Icons.Filled.Close else null,
                    onTailPressed = { formState.onRoomIdChange("") },
                    onValueChange = formState::onRoomIdChange,
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide(); viewModel.getRoom(formState.roomId) }
                    )
                )

                viewModel.state.room?.let {
                    RoomItem(
                        model = it,
                        onPressed = { onRoomSelected(it.id) }
                    )
                }

                if (viewModel.state.error.isNotBlank()){

                    Spacer(
                        modifier = modifier.height(12.dp))

                    ErrorScreen(
                        message = viewModel.state.error.replace("Exception", "."), isSnack = true, isDanger = false)
                }
            }
        }
    )
}