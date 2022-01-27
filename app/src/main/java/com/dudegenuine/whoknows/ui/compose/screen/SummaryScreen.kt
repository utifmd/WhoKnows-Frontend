package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
@ExperimentalFoundationApi @ExperimentalMaterialApi
fun SummaryScreen(
    modifier: Modifier = Modifier,
    event: IRoomEvent) {

    RoomScreen(
        modifier = modifier,
        event = event
    )
}