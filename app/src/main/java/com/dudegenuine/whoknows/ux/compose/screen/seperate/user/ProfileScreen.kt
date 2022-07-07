package com.dudegenuine.whoknows.ux.compose.screen.seperate.user

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.model.common.ImageUtil.asBitmap
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ux.compose.component.GeneralPicture
import com.dudegenuine.whoknows.ux.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ux.compose.component.misc.FieldTag
import com.dudegenuine.whoknows.ux.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ux.compose.state.ResourceState
import com.dudegenuine.whoknows.ux.vm.user.UserViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * Sat, 15 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    viewModel: UserViewModel = hiltViewModel(), state: ResourceState){ /*val state = viewModel.state val formState = viewModel.formState val byteArray = viewModel.formState.profileImage*/
    val context = LocalContext.current
    val scrollState: ScrollState = rememberScrollState()
    val swipeRefreshState = rememberSwipeRefreshState(viewModel.auth.loading || viewModel.state.loading)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { viewModel.userState.onImageValueChange(it, context) })

    Scaffold(modifier.fillMaxSize(),
        topBar = {
            GeneralTopBar(
                title = state.user?.username ?: "",
                leads = if (state.user?.isCurrentUser == true) null else Icons.Filled.ArrowBack,
                onLeadsPressed = if (state.user?.isCurrentUser == true) null else viewModel::onBackPressed,
                tails = Icons.Filled.Share,
                onTailPressed = if (state.user != null){
                    { state.user.id.let(viewModel::onSharePressed) }} else null )}) { padding ->

        SwipeRefresh(swipeRefreshState, { state.user?.id?.let(viewModel::getUser) },
            modifier = Modifier.fillMaxSize().padding(padding)) {
            state.user?.let { user ->
                Column(modifier.verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier.size(12.dp))

                    if (user.isCurrentUser) GeneralPicture(
                        data = if (viewModel.userState.profileImage.isNotEmpty())
                            asBitmap(viewModel.userState.profileImage) else
                                user.profileUrl,
                        onChangePressed = { launcher.launch("image/*") },
                        onCheckPressed = viewModel::onUploadProfile,
                        onPreviewPressed = {
                            viewModel.onPicturePressed(user.profileUrl.substringAfterLast('/'))
                        }
                    ) else GeneralPicture(
                        data = user.profileUrl,
                        onGeneralImagePressed = viewModel::onPicturePressed
                    )
                    Spacer(modifier.size(24.dp))
                    Row {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.Class, tint = MaterialTheme.colors.primary, contentDescription = null)
                            Text("${user.rooms.size} class${if(user.rooms.size > 1)"es" else ""}", fontSize = 11.sp)
                        }
                        Spacer(modifier.size(24.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.Login, tint = MaterialTheme.colors.primary, contentDescription = null)
                            Text("${user.completeParticipants.size} participation ${if(user.completeParticipants.size > 1)"\'s" else ""}", fontSize = 11.sp)
                        }
                    }
                    Spacer(modifier.size(24.dp))
                    Column(
                        contentModifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 12.dp)
                            .clip(shape = MaterialTheme.shapes.medium)
                            .background(color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f))) {

                        FieldTag(
                            key = stringResource(R.string.full_name),
                            editable = user.isCurrentUser,
                            value = user.let{ it.fullName.ifBlank { "Not Set" }},
                            onValuePressed = { viewModel.onFullNamePressed(user.fullName.ifBlank{ "Not Set" })})

                        if (state.user.isCurrentUser) FieldTag(
                            key = stringResource(R.string.phone_number),
                            editable = user.isCurrentUser,
                            value = user.let { it.phone.ifBlank { "Not Set" }},
                            onValuePressed = { viewModel.onPhonePressed(user.phone.ifBlank{ "Not Set" })})

                        FieldTag(
                            key = stringResource(R.string.username),
                            editable = false,
                            value = user.username,
                            onValuePressed = { viewModel.onUsernamePressed(user.username.ifBlank{ "Not Set" }) })

                        if (user.isCurrentUser) FieldTag(
                            key = stringResource(R.string.email),
                            value = user.email,
                            editable = false,
                            onValuePressed = { viewModel.onEmailPressed(user.email) } )

                        FieldTag(
                            key = stringResource(R.string.user_id),
                            value = user.id,
                            editable = false,
                            onValuePressed = { viewModel.onPasswordPressed(user.password) })

                        if (user.isCurrentUser) FieldTag(
                            key = stringResource(R.string.password),
                            value = user.exactPassword,
                            editable = false,
                            censored = true,
                            isDivide = false,
                            onValuePressed = { viewModel.onPasswordPressed(user.password) })
                    }
                    if (user.isCurrentUser){
                        Button(viewModel::onSignOutPressed) {
                            Icon(Icons.Default.Logout, tint = MaterialTheme.colors.onPrimary, contentDescription = null)
                            Spacer(modifier.size(ButtonDefaults.IconSize))
                            Text(stringResource(R.string.sign_out), color = MaterialTheme.colors.onPrimary)
                        }
                    }
                    Spacer(modifier.size(12.dp))
                }
            }
            if (state.error.isNotBlank()) ErrorScreen(message = state.error)
        }
    }
}
