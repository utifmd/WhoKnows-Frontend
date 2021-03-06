package com.dudegenuine.whoknows.ux.compose.screen.seperate.notification

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Login
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.dudegenuine.model.Notification
import com.dudegenuine.model.Participant
import com.dudegenuine.model.User
import com.dudegenuine.whoknows.ux.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ux.compose.component.misc.LazyStatePaging
import com.dudegenuine.whoknows.ux.vm.notification.NotificationViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import okhttp3.internal.http.toHttpDateString

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier, user: User.Complete?,
    lazyPagingItems: LazyPagingItems<Notification>,
    viewModel: NotificationViewModel = hiltViewModel(),
    onUpdated: () -> Unit) {
    val swipeRefreshState = rememberSwipeRefreshState(
        lazyPagingItems.loadState.refresh is LoadState.Loading
    )
    Scaffold(modifier,
        topBar = {
            GeneralTopBar(
                title = "Notifications",
                leads = Icons.Filled.ArrowBack,
                onLeadsPressed = viewModel::onBackPressed
            )
        },
        content = { padding ->
            SwipeRefresh(swipeRefreshState, onRefresh = lazyPagingItems::refresh,
                modifier = modifier.padding(padding).fillMaxSize()) {
                LazyColumn{
                    if (user != null && user.participants.isNotEmpty())
                        bodyParticipated(modifier, user, viewModel::onDetailRoomPressed)

                    if (lazyPagingItems.itemCount > 0) stickyHeader {
                        Text("Recently event${ if(lazyPagingItems.itemCount > 1)"\'s" else ""}", modifier.padding(12.dp, 4.dp)) /*item { LazyStatePaging( modifier = modifier.padding(12.dp, 4.dp), items = lazyPagingItems, vertical = Arrangement.spacedBy(8.dp), repeat = 5, height = 90.dp, width = null ) }*/
                    }
                    item { LazyStatePaging(lazyPagingItems) }
                    items(lazyPagingItems, { it.notificationId }){ item ->
                        if (item != null) NotificationItem(item,
                            onItemLongPressed = { viewModel.onLongPressed(item.notificationId, lazyPagingItems::refresh) }){ hasNeverRead ->
                            viewModel.onNotificationPressed(hasNeverRead, item, onUpdated)
                        }
                    }
                }
            }
        }
    )
}
@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.bodyParticipated(modifier: Modifier = Modifier,
    user: User.Complete, onDetailRoomPressed: (Participant) -> Unit){
    stickyHeader {
        Text("Recently participation${if (user.participants.size > 1) "\'s" else ""}", modifier = modifier.padding(12.dp, 4.dp))
    }
    item {
        Column(modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)) {
            LazyRow(
                modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                items(user.sortedParticipants.size) { i ->
                    val color = if (!user.sortedParticipants[i].expired)
                        MaterialTheme.colors.error else
                            MaterialTheme.colors.primary

                    OutlinedButton(
                        { user.sortedParticipants[i].let(onDetailRoomPressed) },
                        modifier.animateItemPlacement()) {
                        Icon(Icons.Filled.Login, tint = color, contentDescription = null)
                        Spacer(modifier.size(ButtonDefaults.IconSize))
                        Text(user.sortedParticipants[i].createdAt.toHttpDateString(), color = color)
                    }
                }
            }
        }
    }
}