package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomScreen

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
@ExperimentalFoundationApi
@ExperimentalMaterialApi
fun DiscoverScreen(router: NavHostController) {
    RoomScreen(router = router)
}