package com.dudegenuine.whoknows.ui.compose.navigation.graph

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dudegenuine.whoknows.ui.compose.component.misc.DialogSubscriber
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.ProfileEditScreen
import com.dudegenuine.whoknows.ui.vm.user.UserViewModel

/**
 * Tue, 25 Jan 2022
 * WhoKnows by utifmd
 **/
fun NavGraphBuilder.settingGraph(props: IMainProps){
    val router = props.router

    composable(
        route = Screen.Home.Setting.ProfileEditor.routeWithArgs("{filedKey}", "{fieldValue}")){ entry ->
        val viewModel: UserViewModel = hiltViewModel()

        DialogSubscriber(props.vmMain, viewModel)
        ProfileEditScreen(
            viewModel = viewModel,
            onBackPressed = router::popBackStack,
            fieldKey = entry.arguments?.getString("filedKey"),
            fieldValue = entry.arguments?.getString("fieldValue")) {

            router.navigate(Screen.Home.Setting.route){
                popUpTo(Screen.Home.Setting.route){ inclusive = true }
            }
        }
    }
}