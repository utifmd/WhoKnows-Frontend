package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
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
    scaffoldState: ScaffoldState = rememberScaffoldState()) {
    val state = viewModel.state
    val formState = viewModel.formState

    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        topBar = {
            GeneralTopBar(
                title = "Discovery",
                tails = Icons.Default.Notifications
            )
        },

        content = {
            if (state.loading) LoadingScreen()

            state.notifications?.let { notifications ->
                /*val format = SimpleDateFormat("hh:mm aa")
                val calendar = GregorianCalendar.getInstance()

                val groupedNotifies = notifications.groupBy { item ->
                    val date = format.parse(item.createdAt.toString())
                    calendar.time = date

                    calendar.get(Calendar.HOUR_OF_DAY)
                }*/

                LazyColumn(
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    content = {

                        item {
                            Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
                            Text("This Week")
                        }

                        /*repeat(notifications.size) { item { NotificationItem(model = formState.initialModel) } }*/

                        repeat(3) {
                            item { NotificationItem(model = formState.initialModel) }
                        }

                        item {
                            Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
                            Text("Last Year")
                        }

                        repeat(7) {
                            item { NotificationItem(model = formState.initialModel) }
                        }
                    }
                )
            }

            if (state.error.isBlank())
                ErrorScreen(message = state.error)
        }
    )
}