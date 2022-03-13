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

/**
 * Sat, 15 Jan 2022
 * WhoKnows by utifmd
 **/
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
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { viewModel.formState.onImageValueChange(it, context) }
    )
    val swipeRefreshState = rememberSwipeRefreshState(state.loading)

    val byteArray = viewModel.formState.profileImage
    Scaffold(modifier.fillMaxSize(),
        topBar = {
            GeneralTopBar(
                title = state.user?.username ?: "",
                leads = if (!isOwn) Icons.Filled.ArrowBack else null,
                onLeadsPressed = if (!isOwn) event::onBackPressed else null,
                tails = Icons.Filled.MoreVert,
                onTailPressed = { state.user?.let { viewModel.onSharePressed(it.id) } }
            )
        },

        content = {
            SwipeRefresh(swipeRefreshState, onRefresh = { state.user?.let { viewModel.getUser(it.id) } }) {
                if (state.error.isNotBlank()) ErrorScreen(message = state.error)
                else Column(modifier.verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally){

                    Spacer(modifier.size(12.dp))

                    if (isOwn) GeneralPicture(
                        data = if (byteArray.isNotEmpty()) ImageUtil.asBitmap(byteArray)
                            else state.user?.profileUrl ?: "",
                        onChangePressed = { launcher.launch("image/*") },
                        onCheckPressed = viewModel::onUploadProfile
                    ) else GeneralPicture(data = state.user?.profileUrl ?: "")

                    Spacer(modifier.size(12.dp))

                    Row {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.Class, contentDescription = null)
                            Text("${state.user?.rooms?.size} class${if((state.user?.rooms?.size ?: 0) > 1)"es" else ""}", fontSize = 9.sp)
                        }
                        Spacer(modifier.size(24.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.Login, contentDescription = null)
                            Text("${state.user?.participants?.size} participation ${if((state.user?.participants?.size ?: 0) > 1)"\'s" else ""}", fontSize = 9.sp)
                        }
                    }

                    Spacer(modifier.size(12.dp))

                    Column(
                        contentModifier
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
                            value =  state.user?.let { it.fullName.ifBlank { "Not Set" } } ?: "",
                            onValuePressed =
                            { state.user?.let { event.onFullNamePressed(it.fullName) } })

                        FieldTag(
                            key = stringResource(R.string.phone_number),
                            editable = isOwn,
                            value = state.user?.let { it.phone.ifBlank { "Not Set" } } ?: "",
                            onValuePressed = { state.user?.let { event.onPhonePressed(it.phone) } })

                        FieldTag(
                            key = stringResource(R.string.username),
                            editable = isOwn,
                            value = state.user?.username ?: "",
                            onValuePressed =
                            { state.user?.let { event.onUsernamePressed(it.username) }})

                        FieldTag(
                            key = stringResource(R.string.email),
                            value = state.user?.email ?: "",
                            editable = false,
                            onValuePressed = { state.user?.let { event.onEmailPressed(it.email) } })

                        FieldTag(
                            key = stringResource(R.string.user_id),
                            value = state.user?.id ?: "Not set",
                            editable = false,
                            onValuePressed = { state.user?.let { event.onPasswordPressed(it.password) }})

                        FieldTag(
                            key = stringResource(R.string.password),
                            value = state.user?.password ?: "Not set",
                            editable = false,
                            censored = true,
                            isDivide = false,
                            onValuePressed = { state.user?.let { event.onPasswordPressed(it.password) }})
                    }

                    if (isOwn){
                        Button(event::onSignOutPressed) {

                            Icon(Icons.Default.Logout,
                                contentDescription = null)

                            Spacer(modifier.size(ButtonDefaults.IconSize))
                            Text(stringResource(R.string.sign_out))
                        }
                    }

                    Spacer(modifier.size(12.dp))
                }
            }
        }
    )
}
