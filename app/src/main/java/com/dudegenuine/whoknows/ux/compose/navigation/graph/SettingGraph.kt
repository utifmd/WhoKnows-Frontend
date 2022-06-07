package com.dudegenuine.whoknows.ux.compose.navigation.graph

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dudegenuine.whoknows.ux.compose.component.misc.LoggingSubscriber
import com.dudegenuine.whoknows.ux.compose.navigation.Screen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ux.compose.screen.seperate.user.ProfileEditScreen
import com.dudegenuine.whoknows.ux.vm.user.UserViewModel

/**
 * Tue, 25 Jan 2022
 * WhoKnows by utifmd
 **/
fun NavGraphBuilder.settingGraph(props: IMainProps){
    val router = props.router

    composable(
        route = Screen.Home.Setting.ProfileEditor.routeWithArgs("{filedKey}", "{fieldValue}")){ entry ->
        val viewModel: UserViewModel = hiltViewModel()

        LoggingSubscriber(props.vmMain, viewModel)
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