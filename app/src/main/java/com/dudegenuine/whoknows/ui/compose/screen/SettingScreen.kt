package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.ProfileScreen

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
@ExperimentalCoilApi
fun SettingScreen(router: NavHostController) {
    ProfileScreen()

    /*Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Settings screen")
    }*/
}