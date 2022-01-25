package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomScreen

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
@ExperimentalFoundationApi @ExperimentalMaterialApi
fun SummaryScreen(
    modifier: Modifier = Modifier,
    onNewClassPressed: () -> Unit,
    onJoinWithACodePressed: () -> Unit) {

    RoomScreen(
        modifier = modifier,
        onNewClassPressed = onNewClassPressed,
        onJoinWithACodePressed = onJoinWithACodePressed,
    )
}