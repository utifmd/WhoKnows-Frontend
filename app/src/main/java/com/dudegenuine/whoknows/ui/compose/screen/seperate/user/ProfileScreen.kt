package com.dudegenuine.whoknows.ui.compose.screen.seperate.user

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.model.User
import com.dudegenuine.model.common.ImageUtil
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.compose.component.GeneralPicture
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.compose.component.misc.FieldTag
import com.dudegenuine.whoknows.ui.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ui.compose.screen.LoadingScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.IProfileEvent
import com.dudegenuine.whoknows.ui.presenter.user.UserViewModel

/**
 * Sat, 15 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
@ExperimentalCoilApi
fun ProfileScreen(
    modifier: Modifier,
    event: IProfileEvent,
    viewModel: UserViewModel = hiltViewModel()){
    val state = viewModel.state

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            GeneralTopBar(
                title = state.user?.username
                    ?: stringResource(R.string.profile_detail)
            )
        },
        content = {
            if (state.loading){
                LoadingScreen()
            }

            Body(
                modifier = modifier,
                viewModel = viewModel,
                event = event
            )

            if (state.error.isNotBlank()){
                ErrorScreen(
                    message = state.error
                )
            }
        }
    )
}

@Composable
@ExperimentalCoilApi
private fun Body(
    modifier: Modifier,
    viewModel: UserViewModel,
    event: IProfileEvent){

    val context = LocalContext.current
    val user: User = viewModel.state.user ?: return
    val byteArray = viewModel.formState.profileImage

    val scrollState = rememberScrollState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { viewModel.formState.onImageValueChange(it, context) }
    )

    val fullName = user.fullName
        .ifBlank { stringResource(R.string.not_set) }

    val phone = user.phone
        .ifBlank { stringResource(R.string.not_set) }

    Column(
        modifier = modifier.fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally){

        Spacer(
            modifier = Modifier.height(16.dp))

        GeneralPicture(
            data = if (byteArray.isNotEmpty()) ImageUtil.asBitmap(byteArray) else user.profileUrl,
            onChangePressed = { launcher.launch("image/*") },
            onCheckPressed = viewModel::onUploadProfile)

        Spacer(
            modifier = Modifier.height(16.dp))

        Column(
            modifier = modifier.fillMaxWidth()
                .padding(horizontal = 18.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
                )) {

            FieldTag(
                key = stringResource(R.string.full_name),
                value = fullName,
                onValuePressed = { event.onFullNamePressed(fullName) }
            )

            FieldTag(
                key = stringResource(R.string.phone_number),
                value = phone,
                onValuePressed = { event.onPhonePressed(phone) }
            )

            FieldTag(
                key = stringResource(R.string.username),
                value = user.username,
                onValuePressed = { event.onUsernamePressed(user.username) }
            )

            FieldTag(
                key = stringResource(R.string.email),
                value = user.email,
                editable = false,
                onValuePressed = { event.onEmailPressed(user.email) }
            )

            FieldTag(
                key = stringResource(R.string.user_id),
                value = user.id,
                editable = false,
                onValuePressed = { event.onPasswordPressed(user.password) }
            )

            FieldTag(
                key = stringResource(R.string.password),
                value = user.password,
                editable = false,
                censored = true,
                isDivide = false,
                onValuePressed = { event.onPasswordPressed(user.password) }
            )
        }

        TextButton(
            onClick = { event.onSignOutPressed() }) {
            Text(
                text = stringResource(R.string.sign_out), color = MaterialTheme.colors.error
            )
        }
    }
}
