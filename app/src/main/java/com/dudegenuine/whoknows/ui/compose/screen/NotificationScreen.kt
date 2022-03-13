package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ui.compose.screen.seperate.notification.NotificationItem
import com.dudegenuine.whoknows.ui.vm.notification.NotificationViewModel

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
@ExperimentalCoilApi
@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onBackPressed: () -> Unit) {
    val state = viewModel.state //val formState = viewModel.formState

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
            if (state.loading) LoadingScreen()

            state.notifications?.let { notifications ->

                LazyColumn(
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    content = {

                        item {
                            Spacer(modifier.size(ButtonDefaults.IconSize))
                            Text("Recently event${ if(notifications.size > 1)"\'s" else ""}")
                        }

                        notifications.forEach { model ->
                            item { NotificationItem(model = model)
                                { viewModel.onNotificationPressed(model) }
                            }
                        }
                    }
                )
            }

            if (state.error.isBlank())
                ErrorScreen(message = state.error)
        }
    )
}