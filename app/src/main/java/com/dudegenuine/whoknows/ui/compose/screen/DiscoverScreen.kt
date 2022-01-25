package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun DiscoverScreen(modifier: Modifier = Modifier, router: NavHostController) {
    ErrorScreen(message = "Discover Screen", isDanger = false)
}