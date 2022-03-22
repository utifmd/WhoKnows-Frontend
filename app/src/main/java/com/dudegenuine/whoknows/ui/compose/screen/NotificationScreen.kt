package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Login
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.model.Participant
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.compose.screen.seperate.notification.NotificationItem
import com.dudegenuine.whoknows.ui.vm.notification.NotificationViewModel
import com.dudegenuine.whoknows.ui.vm.user.UserViewModel
import kotlinx.coroutines.FlowPreview
import okhttp3.internal.http.toHttpDateString

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
@FlowPreview
@ExperimentalCoilApi
@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    vmUser: UserViewModel = hiltViewModel(),
    vmNotifier: NotificationViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onDetailRoomPressed: (Participant) -> Unit, onBackPressed: () -> Unit) {
    val stateNotifier = vmNotifier.state
    val stateUser = vmUser.state

    Scaffold(modifier,
        scaffoldState = scaffoldState,
        topBar = {
            GeneralTopBar(
                title = "Notifications",
                leads = Icons.Filled.ArrowBack,
                onLeadsPressed = onBackPressed
            )
        },

        content = {
            if (stateNotifier.loading || stateUser.loading) LoadingScreen()
            LazyColumn(
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    stateUser.user?.let { user ->
                        if (user.participants.isNotEmpty()) {
                            if (user.participants.isNotEmpty()) item {
                                Text("Recently participation${if (user.participants.size > 1) "\'s" else ""}")
                            }
                            item {
                                LazyRow(modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)) {

                                    user.participants.forEach { participant ->
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

                    stateNotifier.notifications?.let { notifications ->
                        if (notifications.isNotEmpty()) item {
                            Spacer(modifier.size(ButtonDefaults.IconSize))
                            Text("Recently event${ if(notifications.size > 1)"\'s" else ""}")
                        }

                        notifications.forEach { model ->
                            item { NotificationItem(model = model) { vmNotifier.onNotificationDecrease(model) } }
                        }
                    }
                }
            )
            if (stateNotifier.error.isNotBlank())
                ErrorScreen(message = stateNotifier.error, isDanger = false)
        }
    )
}