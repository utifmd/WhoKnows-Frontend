package com.dudegenuine.whoknows.ux.compose.screen.seperate.room

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.InsertInvitation
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ux.compose.component.GeneralTextField
import com.dudegenuine.whoknows.ux.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ux.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ux.vm.room.RoomViewModel

/**
 * Mon, 17 Jan 2022
 * WhoKnows by utifmd
 **/
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun RoomFinderScreen(
    modifier: Modifier = Modifier,
    viewModel: RoomViewModel = hiltViewModel()) {
    val focusRequester = remember{ FocusRequester() }
    val formState = viewModel.roomState
    fun submit() = viewModel.onRoomFinderButtonGoPressed(formState.roomId)
    LaunchedEffect(Unit){ focusRequester.requestFocus() }
    Scaffold(modifier.fillMaxSize(),
        topBar = {
            GeneralTopBar(
                title = stringResource(R.string.join_with_a_code),
                leads = Icons.Filled.ArrowBack,
                onLeadsPressed = viewModel::onBackPressed,
                tails = Icons.Default.Send,
                submitEnable = formState.roomId.isNotBlank(),
                submitLoading = viewModel.state.loading,
                onSubmitPressed = ::submit
            )
        },
        content = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier.padding(12.dp)) {

                GeneralTextField(
                    modifier.focusRequester(focusRequester),
                    label = "Enter an invitation code",
                    value = formState.roomId,
                    leads = Icons.Filled.InsertInvitation,
                    trail = if(formState.roomId.isNotBlank()) Icons.Filled.Close else null,
                    onTailPressed = { formState.onRoomIdChange("") },
                    onValueChange = formState::onRoomIdChange,
                    keyboardActions = KeyboardActions{ submit() }
                )

                /*viewModel.state.room?.let {
                    RoomItem(
                        model = it,
                        onPressed = { onRoomSelected(it.id) },
                        onImpressed = {}
                    )
                }*/

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