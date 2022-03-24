package com.dudegenuine.whoknows.ui.compose.component.misc

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.dudegenuine.whoknows.ui.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ui.compose.screen.LoadBoxScreen

@Composable
fun <T: Any> LazyStatePaging(
    modifier: Modifier = Modifier,
    height: Dp = 125.dp, width: Dp? = 246.dp,
    items: LazyPagingItems<T>,
    vertical: Arrangement.Vertical? = null,
    horizontal: Arrangement.Horizontal? = null, repeat: Int){

    with(items) { when {
        loadState.append is LoadState.Loading ||
                loadState.refresh is LoadState.Loading -> when {
            vertical != null -> Column(verticalArrangement = vertical) {
                repeat(repeat) { LoadBoxScreen(height = height, width = width) }}

            horizontal != null -> Row(horizontalArrangement = horizontal) {
                repeat(repeat) { LoadBoxScreen(height = height, width = width) }}

            else -> LoadBoxScreen(height = height, width = width)
        }
        loadState.append is LoadState.Error ||
                loadState.refresh is LoadState.Error -> {

            ErrorScreen(modifier.clickable(onClick = ::refresh), isSnack = true,
                message = (loadState.refresh as LoadState.Error).error.localizedMessage ?: "refresh")
        }
        loadState.source.refresh is LoadState.NotLoading ||
                loadState.source.append is LoadState.NotLoading -> {

            if (items.itemCount < 1) ErrorScreen(modifier,
                message = "No result.", isDanger = false, isSnack = true)
        }
    }}
}