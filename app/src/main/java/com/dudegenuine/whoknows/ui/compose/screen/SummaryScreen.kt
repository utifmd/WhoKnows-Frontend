package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEventHome

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
@ExperimentalFoundationApi @ExperimentalMaterialApi
fun SummaryScreen(
    event: IRoomEventHome) {

    RoomScreen(
        event = event
    )
}