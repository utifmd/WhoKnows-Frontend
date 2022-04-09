package com.dudegenuine.whoknows.ui.compose.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.ProfileEditScreen

/**
 * Tue, 25 Jan 2022
 * WhoKnows by utifmd
 **/
fun NavGraphBuilder.settingGraph(
    router: NavHostController){

    composable(
        route = Screen.Home.Setting.ProfileEditor.routeWithArgs("{filedKey}", "{fieldValue}")){ entry ->

        ProfileEditScreen(
            onBackPressed = router::popBackStack,
            fieldKey = entry.arguments?.getString("filedKey"),
            fieldValue = entry.arguments?.getString("fieldValue")) {

            router.navigate(Screen.Home.Setting.route){
                popUpTo(Screen.Home.Setting.route){ inclusive = true }
            }
        }
    }
}