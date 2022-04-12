package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Login
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.model.Participant
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ui.compose.screen.seperate.notification.NotificationItem
import com.dudegenuine.whoknows.ui.compose.state.DialogState
import com.dudegenuine.whoknows.ui.vm.notification.NotificationViewModel
import com.dudegenuine.whoknows.ui.vm.user.UserViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import okhttp3.internal.http.toHttpDateString

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotificationScreen(props: IMainProps,
    modifier: Modifier = Modifier,
    vmUser: UserViewModel = hiltViewModel(),
    vmNotifier: NotificationViewModel = hiltViewModel(),
    onDetailRoomPressed: (Participant) -> Unit,
    onBackPressed: () -> Unit, onPressed: (String, String) -> Unit) {
    val swipeRefreshState = rememberSwipeRefreshState(
        vmNotifier.state.loading || vmUser.state.loading)
    val onRefresh: () -> Unit = {
        vmNotifier.apply {
            currentUserId.let(vmUser::getUser)
            getNotifications() }}
    val onLongPressed: (String) -> Unit = { notId ->
        val dialog = DialogState(props.context.getString(R.string.delete_notifier),
            onSubmitted = { vmNotifier.deleteNotification(notId) })
        props.vmMain.onDialogStateChange(dialog)
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

                                        items(user.sortedParticipants, { it.id }) { model ->
                                            OutlinedButton(
                                                { onDetailRoomPressed(model) },
                                                modifier.animateItemPlacement()) {

                                                Icon(Icons.Filled.Login, tint = MaterialTheme.colors.primary, contentDescription = null)
                                                Spacer(modifier.size(ButtonDefaults.IconSize))
                                                Text(model.createdAt.toHttpDateString())
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

                            items(notifications, { it.notificationId }) { model ->
                                NotificationItem(modifier
                                    .animateItemPlacement(), model,
                                    onItemLongPressed = { onLongPressed(model.notificationId) },
                                    onItemPressed = { vmNotifier.onReadNotification(model)
                                        { onPressed(model.roomId, model.userId) }
                                    }
                                )
                            }
                        }
                    }
                )
                if (vmNotifier.state.error.isNotBlank())
                    ErrorScreen(modifier, message = vmNotifier.state.error, isDanger = false)
            }
        }
    )

    /*LaunchedEffect(vmNotifier.currentUserId) {
        vmNotifier.currentUserId.let(vmUser::getUser)
    }*/
}