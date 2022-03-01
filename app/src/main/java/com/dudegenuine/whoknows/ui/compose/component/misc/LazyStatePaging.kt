package com.dudegenuine.whoknows.ui.compose.component.misc

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.dudegenuine.whoknows.ui.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ui.compose.screen.LoadBoxScreen

@Composable
fun <T: Any> LazyStatePaging(
    modifier: Modifier = Modifier, items: LazyPagingItems<T>, times: Int? = null){

    with (items) {
        when {
            loadState.append is LoadState.Loading -> if (times != null) Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)) {

                repeat(times){ LoadBoxScreen(height = 125.dp, width = 246.dp) }
            } else LoadBoxScreen(height = 125.dp, width = 246.dp)

            loadState.refresh is LoadState.Loading ->if (times != null) Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)) {

                repeat(times){ LoadBoxScreen(height = 125.dp, width = 246.dp) }
            } else LoadBoxScreen(height = 125.dp, width = 246.dp)

            loadState.append is LoadState.Error -> {
                val e = loadState.append as LoadState.Error

                ErrorScreen(modifier.clickable(onClick = ::retry),
                    message = e.error.localizedMessage ?: "retry",
                    isSnack = true
                )
            }

            loadState.refresh is LoadState.Error -> {
                val e = loadState.refresh as LoadState.Error

                ErrorScreen(modifier.clickable(onClick = ::retry),
                    message = e.error.localizedMessage ?: "retry",
                    isSnack = true
                )
            }
        }
    }
}