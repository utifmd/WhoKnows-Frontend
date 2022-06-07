package com.dudegenuine.whoknows.ux.compose.screen

import androidx.compose.runtime.Composable
import com.dudegenuine.whoknows.ux.compose.screen.seperate.user.ProfileScreen
import com.dudegenuine.whoknows.ux.compose.state.ResourceState
import com.dudegenuine.whoknows.ux.vm.user.UserViewModel

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun SettingScreen(
    viewModel: UserViewModel, state: ResourceState) {

    ProfileScreen(
        viewModel = viewModel,
        state = state
    )
}