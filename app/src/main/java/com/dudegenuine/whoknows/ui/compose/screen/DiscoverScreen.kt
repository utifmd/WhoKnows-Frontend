package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomScreen

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
fun DiscoverScreen(modifier: Modifier = Modifier, router: NavHostController) {
    RoomScreen(modifier = modifier, router = router)
}