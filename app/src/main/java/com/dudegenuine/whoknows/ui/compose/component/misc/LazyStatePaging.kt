package com.dudegenuine.whoknows.ui.compose.component.misc

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.dudegenuine.model.Resource
import com.dudegenuine.whoknows.ui.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ui.compose.screen.LoadBoxScreen

@Composable
fun <T: Any> LazyStatePaging(
    modifier: Modifier = Modifier,
    height: Dp = 125.dp, width: Dp? = 246.dp,
    items: LazyPagingItems<T>,
    vertical: Arrangement.Vertical? = null,
    horizontal: Arrangement.Horizontal? = null, repeat: Int){
    //var loaded by remember { mutableStateOf(false)  }

    with(items) {
        when {
            loadState.prepend is LoadState.Loading ||
                    loadState.refresh is LoadState.Loading -> {
                //loaded = true
                when {
                    vertical != null -> Column(verticalArrangement = vertical) {
                        repeat(repeat) { LoadBoxScreen(height = height, width = width) }
                    }

                    horizontal != null -> Row(horizontalArrangement = horizontal) {
                        repeat(repeat) { LoadBoxScreen(height = height, width = width) }
                    }

                    else -> LoadBoxScreen(height = height, width = width)
                }
            }
            loadState.refresh is LoadState.Error-> {
                val error = (loadState.refresh as LoadState.Error).error.localizedMessage ?: "Retry again"
                Box(if (width != null) modifier
                    .height(height)
                    .width(width) else modifier
                    .height(height)
                    .fillMaxWidth(),
                    contentAlignment = Alignment.Center){
                    ErrorScreen(
                        message = error,
                        onPressed = items::retry,
                        isDanger = error != Resource.NO_RESULT
                    )
                }
            }
        }
    }
}