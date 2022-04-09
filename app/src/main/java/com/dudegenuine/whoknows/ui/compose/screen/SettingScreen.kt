package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.ProfileScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.IProfileEvent
import kotlinx.coroutines.FlowPreview

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    event: IProfileEvent) {

    ProfileScreen(
        modifier = modifier,
        event = event,
        isOwn = true
    )
}