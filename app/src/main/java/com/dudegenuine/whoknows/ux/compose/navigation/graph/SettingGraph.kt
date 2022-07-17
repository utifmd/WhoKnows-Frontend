package com.dudegenuine.whoknows.ux.compose.navigation.graph

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dudegenuine.whoknows.ux.compose.component.misc.LoggingSubscriber
import com.dudegenuine.whoknows.ux.compose.navigation.Screen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ux.compose.screen.seperate.user.ProfileEditScreen
import com.dudegenuine.whoknows.ux.vm.user.UserViewModel
import com.dudegenuine.whoknows.ux.vm.user.contract.IProfileEvent.Companion.KEY_EDIT_FIELD_TYPE
import com.dudegenuine.whoknows.ux.vm.user.contract.IProfileEvent.Companion.KEY_EDIT_FIELD_VALUE

/**
 * Tue, 25 Jan 2022
 * WhoKnows by utifmd
 **/
fun NavGraphBuilder.settingGraph(props: IMainProps){

    composable(
        route = Screen.Home.Setting.ProfileEditor.routeWithArgs("{$KEY_EDIT_FIELD_TYPE}", "{$KEY_EDIT_FIELD_VALUE}")){// entry ->
        val viewModel: UserViewModel = hiltViewModel()

        LoggingSubscriber(props.viewModel, viewModel)
        ProfileEditScreen(viewModel = viewModel, currentState = props.viewModel.auth.user)
    }
}