package com.dudegenuine.whoknows.ux.compose.screen.seperate.room

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.items
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ux.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ux.compose.component.misc.LazyStatePaging
import com.dudegenuine.whoknows.ux.compose.model.BottomDomain
import com.dudegenuine.whoknows.ux.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ux.vm.room.RoomViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * Wed, 29 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
fun RoomHomeScreen(
    viewModel: RoomViewModel,
    modifier: Modifier = Modifier, props: IMainProps) {
    val swipeRefreshState = rememberSwipeRefreshState(
        props.lazyPagingRoomComplete.loadState.refresh is LoadState.Loading
    )
    val badge by remember(props.viewModel.auth.user){
        mutableStateOf(props.viewModel.auth.user?.badge ?: 0)
    }
    fun onRefresh() = props.run {
        lazyPagingRoomCensored.refresh()
        lazyPagingRoomComplete.refresh()
    }
    val topBar: @Composable () -> Unit = {
        GeneralTopBar(
            title = BottomDomain.SUMMARY,
            tails = if (badge > 0) Icons.Filled.Notifications else Icons.Outlined.Notifications,
            tailsTint = if (badge > 0) MaterialTheme.colors.error else null,
            onTailPressed = viewModel::onNotificationPressed
        )
    }
    val content: @Composable (PaddingValues) -> Unit = {
        val header: @Composable LazyItemScope.() -> Unit = {
            Header(modifier,
                onNewClassPressed = viewModel::onNewClassPressed,
                onJoinWithACodePressed = viewModel::onButtonJoinRoomWithACodePressed)
        }
        val state: @Composable LazyItemScope.() -> Unit = {
            LazyStatePaging(
                items = props.lazyPagingRoomComplete,
                vertical = Arrangement.spacedBy(8.dp),
                repeat = 5, height = 130.dp, width = null)
        }
        val body: LazyListScope.() -> Unit = {
            items(props.lazyPagingRoomComplete, { it.id }) { room -> if(room != null)
                RoomItem(model = room) { viewModel.onRoomHomeScreenDetailSelected(room.id) }  /*, onImpression = { impressed -> with(props.viewModel as MainViewModel){ onImpression(impressed, room, ::onRefresh) } }*/
            }
        }
        SwipeRefresh(swipeRefreshState, ::onRefresh) {
            LazyColumn(modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {

                stickyHeader(content = header)
                item(content = state)
                body()
            }
        }
    }
    Scaffold(modifier, topBar = topBar, content = content)
}
@Composable
private fun Header(
    modifier: Modifier = Modifier,
    onNewClassPressed: () -> Unit,
    onJoinWithACodePressed: () -> Unit){

    Row(modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            modifier = modifier.weight(1f),
            onClick = onNewClassPressed) {
            Text(text = stringResource(R.string.new_class))
        }
        OutlinedButton(
            modifier = modifier.weight(1f),
            onClick = onJoinWithACodePressed) {
            Text(text = stringResource(R.string.join_with_a_code))
        }
    }
}