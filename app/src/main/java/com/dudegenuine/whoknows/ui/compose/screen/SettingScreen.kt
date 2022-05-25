package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.ProfileScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.IProfileEvent
import com.dudegenuine.whoknows.ui.vm.user.UserViewModel

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel,
    event: IProfileEvent) {

    ProfileScreen(modifier,
        viewModel = viewModel,
        event = event,
        isOwn = true
    )
}