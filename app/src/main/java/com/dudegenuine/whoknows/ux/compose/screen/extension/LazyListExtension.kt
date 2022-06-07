package com.dudegenuine.whoknows.ux.compose.screen.extension

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.paging.LoadState

/**
 * Tue, 07 Jun 2022
 * WhoKnows by utifmd
 **/
fun LazyListScope.pagingLoadStateItem(
    loadState: LoadState,
    keySuffix: String? = null,
    loading: (@Composable LazyItemScope.() -> Unit)? = null,
    error: (@Composable LazyItemScope.(LoadState.Error) -> Unit)? = null,
) {
    if (loading != null && loadState == LoadState.Loading) {
        item(
            key = keySuffix?.let { "loadingItem_$it" },
            content = loading,
        )
    }
    if (error != null && loadState is LoadState.Error) {
        item(
            key = keySuffix?.let { "errorItem_$it" },
            content = { error(loadState)},
        )
    }
}