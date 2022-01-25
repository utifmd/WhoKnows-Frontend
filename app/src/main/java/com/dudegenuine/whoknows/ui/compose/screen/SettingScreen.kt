package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.ProfileScreen

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
@ExperimentalCoilApi
fun SettingScreen(
    modifier: Modifier = Modifier,
    onSignOutPressed: () -> Unit) {

    ProfileScreen(
        modifier = modifier,
        onSignOutPressed = onSignOutPressed
    )
}