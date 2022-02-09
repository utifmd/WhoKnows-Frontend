package com.dudegenuine.whoknows.ui.compose.screen.seperate.user

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.local.api.INotificationService
import com.dudegenuine.model.common.ImageUtil
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.infrastructure.di.android.api.NotificationService
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
// TODO: Public able
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
@ExperimentalCoilApi
fun ProfileScreen(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    isOwn: Boolean,
    event: IProfileEvent,
    viewModel: UserViewModel = hiltViewModel()){

    val context = LocalContext.current
    val state = viewModel.state
    val byteArray = viewModel.formState.profileImage

    val scrollState = rememberScrollState()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { viewModel.formState.onImageValueChange(it, context) }
    )

    val onStartService: () -> Unit = {
        Log.d("ProfileScreen: ", "onStartService..")
        Intent(context, NotificationService::class.java)
            .putExtra(INotificationService.TIME_KEY, 30.0).apply(
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) this::startForegroundService
                else*/ context::startService
            )
    }

    if (state.loading) LoadingScreen()

    state.user?.let { user ->
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                GeneralTopBar(
                    title = state.user.username
                )
            },

            content = {
                Column(
                    modifier = modifier.verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally){

                    Spacer(
                        modifier = modifier.size(12.dp))

                    if (isOwn) GeneralPicture(
                        data = if (byteArray.isNotEmpty())
                            ImageUtil.asBitmap(byteArray) else user.profileUrl,
                        onChangePressed = { launcher.launch("image/*") },
                        onCheckPressed = viewModel::onUploadProfile
                    ) else GeneralPicture(
                        data = user.profileUrl
                    )

                    Spacer(
                        modifier = modifier.size(36.dp))

                    Column(
                        modifier = contentModifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 8.dp, horizontal = 12.dp
                            )
                            .clip(
                                shape = MaterialTheme.shapes.medium
                            )
                            .background(
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
                            )) {

                        FieldTag(
                            key = stringResource(R.string.full_name),
                            editable = isOwn,
                            value = user.fullName.ifBlank { "Not set" },
                            onValuePressed = { event.onFullNamePressed(user.fullName.ifBlank { "Not set" }) }
                        )

                        FieldTag(
                            key = stringResource(R.string.phone_number),
                            editable = isOwn,
                            value = user.phone.ifBlank { "Not set" },
                            onValuePressed = { event.onPhonePressed(user.phone.ifBlank { "Not set" }) }
                        )

                        FieldTag(
                            key = stringResource(R.string.username),
                            editable = isOwn,
                            value = user.username,
                            onValuePressed = onStartService //{ event.onUsernamePressed(user.username) }
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

                    if (isOwn){
                        Button(
                            onClick = event::onSignOutPressed) {

                            Icon(
                                imageVector = Icons.Default.Logout, contentDescription = null
                            )

                            Spacer(modifier.size(ButtonDefaults.IconSize))
                            Text(stringResource(R.string.sign_out))
                        }
                    }

                    Spacer(modifier = modifier.size(12.dp))
                }
            }
        )
    }

    if (state.error.isNotBlank()) ErrorScreen(message = state.error)

}
