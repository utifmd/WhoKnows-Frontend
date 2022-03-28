package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Login
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.model.Participant
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.compose.screen.seperate.notification.NotificationItem
import com.dudegenuine.whoknows.ui.compose.state.DialogState
import com.dudegenuine.whoknows.ui.vm.notification.NotificationViewModel
import com.dudegenuine.whoknows.ui.vm.user.UserViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.FlowPreview
import okhttp3.internal.http.toHttpDateString

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
@ExperimentalFoundationApi
@FlowPreview
@ExperimentalCoilApi
@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    vmUser: UserViewModel = hiltViewModel(),
    vmNotifier: NotificationViewModel = hiltViewModel(),
    onDetailRoomPressed: (Participant) -> Unit,
    onBackPressed: () -> Unit, onPressed: (String, String) -> Unit) {
    var dialogState by remember { mutableStateOf(DialogState()) }
    val swipeRefreshState = rememberSwipeRefreshState(
        vmNotifier.state.loading || vmUser.state.loading)
    val onRefresh: () -> Unit = {
        vmNotifier.apply {
            currentUserId.let(vmUser::getUser)
            getNotifications()
        }
    }

    val onLongPressed: (String) -> Unit = { notId ->
        dialogState = DialogState("hapus notification", true,
            onDismissed = { dialogState = DialogState() },
            onSubmitted = { vmNotifier.deleteNotification(notId) }
        )
    }

    Scaffold(modifier,
        scaffoldState = vmNotifier.scaffoldState,
        topBar = {
            GeneralTopBar(
                title = "Notifications",
                leads = Icons.Filled.ArrowBack,
                onLeadsPressed = onBackPressed
            )
        },

        content = {
            SwipeRefresh(swipeRefreshState, onRefresh = onRefresh) {

                LazyColumn( contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    content = {
                        vmUser.state.user?.let { user ->
                            if (user.participants.isNotEmpty()) {
                                if (user.participants.isNotEmpty()) item {
                                    Text("Recently participation${if (user.participants.size > 1) "\'s" else ""}")
                                }
                                item {
                                    LazyRow(modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)) {

                                        user.participants.sortedByDescending { it.createdAt }
                                            .forEach { participant ->

                                            item {
                                                OutlinedButton(onClick = { onDetailRoomPressed(participant) }) {
                                                    Icon(Icons.Filled.Login, tint = MaterialTheme.colors.primary, contentDescription = null)
                                                    Spacer(modifier.size(ButtonDefaults.IconSize))
                                                    Text(participant.createdAt.toHttpDateString())
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        vmNotifier.state.notifications?.let { notifications ->
                            if (notifications.isNotEmpty()) item {
                                Spacer(modifier.size(ButtonDefaults.IconSize))
                                Text("Recently event${ if(notifications.size > 1)"\'s" else ""}")
                            }

                            notifications.forEach { model -> item {
                                NotificationItem(
                                    model = model,
                                    onItemLongPressed = { onLongPressed(model.notificationId) },
                                    onItemPressed = { vmNotifier.onReadNotification(model)
                                        { onPressed(model.roomId, model.userId) }})
                            }}
                        }
                    }
                )
                if (vmNotifier.state.error.isNotBlank())
                    ErrorScreen(message = vmNotifier.state.error, isDanger = false)
                if (dialogState.opened) with (dialogState) {
                    AlertDialog(
                        modifier = modifier.padding(horizontal = 24.dp),
                        onDismissRequest = { onDismissed?.invoke() },
                        title = { Text(title) },
                        text = { Text(text) },
                        confirmButton = {
                            TextButton(
                                { onSubmitted?.invoke(); onDismissed?.invoke() },
                                enabled = onSubmitted != null) {

                                Text((button ?: "submit").replaceFirstChar { it.uppercase() })
                            }
                        }
                    )
                }
            }
        }
    )

    /*LaunchedEffect(vmNotifier.currentUserId) {
        vmNotifier.currentUserId.let(vmUser::getUser)
    }*/
}