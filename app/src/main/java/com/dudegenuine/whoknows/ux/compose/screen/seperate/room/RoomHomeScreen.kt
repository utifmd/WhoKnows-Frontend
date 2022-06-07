package com.dudegenuine.whoknows.ux.compose.screen.seperate.room

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.items
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ux.compose.component.GeneralTopBar
import com.dudegenuine.whoknows.ux.compose.component.misc.LazyStatePaging
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
fun RoomHomeScreen(viewModel: RoomViewModel,
    /*event: IRoomEventHome, */modifier: Modifier = Modifier, props: IMainProps) {
    val swipeRefreshState = rememberSwipeRefreshState(
        props.lazyPagingOwnerRooms.loadState.refresh is LoadState.Loading)

    Scaffold(modifier,
        topBar = { GeneralTopBar(title = "Created class") }) {
        SwipeRefresh(swipeRefreshState, props.lazyPagingOwnerRooms::refresh) {
            LazyColumn(modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {

                stickyHeader {
                    Header(
                        modifier = modifier,
                        onNewClassPressed = viewModel::onNewClassPressed,
                        onJoinWithACodePressed = viewModel::onButtonJoinRoomWithACodePressed,
                    )
                }

                item {
                    LazyStatePaging(
                        items = props.lazyPagingOwnerRooms,
                        vertical = Arrangement.spacedBy(8.dp),
                        repeat = 5, height = 130.dp, width = null
                    )
                }

                if(props.lazyPagingOwnerRooms.loadState.refresh !is LoadState.Error) {
                    items(props.lazyPagingOwnerRooms, { it.id }) { room ->
                        if (room != null) RoomItem(
                            model = room,
                            onImpressed = viewModel::onImpressed) {
                            viewModel.onRoomItemSelected(room.id)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    onNewClassPressed: () -> Unit,
    onJoinWithACodePressed: () -> Unit){

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()) {

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