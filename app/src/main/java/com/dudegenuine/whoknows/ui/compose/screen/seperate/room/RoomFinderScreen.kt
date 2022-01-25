package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.compose.component.GeneralTextField
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ui.presenter.room.RoomViewModel

/**
 * Mon, 17 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun RoomFinderScreen(
    modifier: Modifier = Modifier,
    viewModel: RoomViewModel = hiltViewModel()) {
    val formState = viewModel.formState

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            GeneralTopBar(
                title = stringResource(
                    id = R.string.join_with_a_code
                ),
                submitLabel = "Join",
                submitEnable = formState.isGetValid,
                submitLoading = viewModel.state.loading,
                onSubmitPressed = viewModel::findRoom
            )
        },
        content = {
            Column(
                modifier = modifier.padding(16.dp)) {

                GeneralTextField(
                    label = "Enter an invitation code",
                    value = formState.roomId,
                    leads = Icons.Default.Security,
                    tails = if(formState.roomId.isNotBlank()) Icons.Default.Close else null,
                    onTailPressed = { formState.onRoomIdChange("") },
                    onValueChange = formState::onRoomIdChange
                )

                viewModel.state.room?.let {

                    Spacer(modifier = modifier.height(12.dp))
                    RoomItem(state = it)
                }

                if (viewModel.state.error.isNotBlank()){

                    Spacer(modifier = modifier.height(12.dp))
                    ErrorScreen(message = viewModel.state.error, isSnack = true, isDanger = false)
                }
            }
        }
    )
}