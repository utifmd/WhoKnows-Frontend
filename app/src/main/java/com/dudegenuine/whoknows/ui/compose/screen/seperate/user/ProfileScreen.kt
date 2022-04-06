package com.dudegenuine.whoknows.ui.compose.screen.seperate.user

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.model.common.ImageUtil
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.compose.component.GeneralPicture
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.compose.component.misc.FieldTag
import com.dudegenuine.whoknows.ui.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.IProfileEvent
import com.dudegenuine.whoknows.ui.vm.user.UserViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.FlowPreview

/**
 * Sat, 15 Jan 2022
 * WhoKnows by utifmd
 **/
@FlowPreview
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
@ExperimentalCoilApi
fun ProfileScreen(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    isOwn: Boolean,
    event: IProfileEvent,
    viewModel: UserViewModel = hiltViewModel(),
    scrollState: ScrollState = rememberScrollState()){

    val state = viewModel.state
    val formState = viewModel.formState
    val byteArray = formState.profileImage
    val context = LocalContext.current
    val swipeRefreshState = rememberSwipeRefreshState(state.loading)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { formState.onImageValueChange(it, context) })

    Scaffold(modifier.fillMaxSize(),
        topBar = {
            GeneralTopBar(
                title = state.user?.username ?: "",
                leads = if (!isOwn) Icons.Filled.ArrowBack else null,
                onLeadsPressed = if (!isOwn) event::onBackPressed else null,
                tails = Icons.Filled.MoreVert,
                onTailPressed = if (state.user != null){
                    { state.user.id.let(viewModel::onSharePressed) }} else null
            )
        },

        content = {
            SwipeRefresh(swipeRefreshState, { state.user?.id?.let(viewModel::getUser) }) {
                state.user?.let { user ->
                    val profileUrl = user.profileUrl.substringAfterLast('/')
                    Column(modifier.verticalScroll(scrollState),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier.size(12.dp))

                        if (isOwn) GeneralPicture(
                            data = if (byteArray.isNotEmpty()) ImageUtil.asBitmap(byteArray)
                                else profileUrl,
                            onChangePressed = { launcher.launch("image/*") },
                            onCheckPressed = viewModel::onUploadProfile,
                            onPreviewPressed = {
                                val fileId = profileUrl
                                event.onPicturePressed(fileId)
                            }

                        ) else GeneralPicture(
                            data = user.profileUrl,
                            onGeneralImagePressed = event::onPicturePressed
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
                                Text("${user.participants.size} participation ${if(user.participants.size > 1)"\'s" else ""}", fontSize = 11.sp)
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
                                editable = isOwn,
                                value = user.let { it.fullName.ifBlank { "Not Set" } },
                                onValuePressed = { user.let { event.onFullNamePressed(it.fullName.ifBlank { "Not Set" }) } })

                            FieldTag(
                                key = stringResource(R.string.phone_number),
                                editable = isOwn,
                                value = user.let { it.phone.ifBlank { "Not Set" } },
                                onValuePressed = { user.let { event.onPhonePressed(it.phone.ifBlank { "Not Set" }) } })

                            FieldTag(
                                key = stringResource(R.string.username),
                                editable = false,
                                value = user.username,
                                onValuePressed = { user.let { event.onUsernamePressed(it.username.ifBlank { "Not Set" }) }})

                            FieldTag(
                                key = stringResource(R.string.email),
                                value = user.email,
                                editable = false,
                                onValuePressed = { user.let { event.onEmailPressed(it.email) } })

                            FieldTag(
                                key = stringResource(R.string.user_id),
                                value = user.id,
                                editable = false,
                                onValuePressed = { user.let { event.onPasswordPressed(it.password) }})

                            FieldTag(
                                key = stringResource(R.string.password),
                                value = user.password,
                                editable = false,
                                censored = true,
                                isDivide = false,
                                onValuePressed = { user.let { event.onPasswordPressed(it.password) }})
                        }

                        if (isOwn){
                            Button({ event.onSignOutPressed(onSubmitted = viewModel::signOutUser) }) {
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
    )
}
