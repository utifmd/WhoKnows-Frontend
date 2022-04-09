package com.dudegenuine.whoknows.ui.compose.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.dudegenuine.whoknows.ui.compose.component.misc.ImageViewer
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.DiscoverScreen
import com.dudegenuine.whoknows.ui.compose.screen.SettingScreen
import com.dudegenuine.whoknows.ui.compose.screen.SummaryScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.ProfileEvent
import com.dudegenuine.whoknows.ui.vm.file.IFileViewModel.Companion.PREVIEW_FILE_ID

/**
 * Wed, 19 Jan 2022
 * WhoKnows by utifmd
 **/
fun NavGraphBuilder.homeNavGraph(props: IMainProps) {
    val preview = Screen.Home.Preview

    navigation(
        route = Screen.Home.route,
        startDestination = Screen.Home.Summary.route) {

        discoverGraph(props)
        composable(
            route = Screen.Home.Discover.route) {

            DiscoverScreen(props)
        }

        summaryGraph(props)
        composable(
            route = Screen.Home.Summary.route) {

            SummaryScreen(props)
        }

        settingGraph(props.router)
        composable(
            route = Screen.Home.Setting.route) {

            SettingScreen(
                event = ProfileEvent(props)
            )
        }

        composable(
            route = preview.routeWithArgs("{$PREVIEW_FILE_ID}"),
            deepLinks = listOf( navDeepLink {
                uriPattern = preview.uriWithArgs("{$PREVIEW_FILE_ID}") })) { entry ->

            ImageViewer(
                onBackPressed = props.router::popBackStack,
                fileId = entry.arguments?.getString(PREVIEW_FILE_ID) ?: "bacd3011-8aa5-4742-bf3f-be65ddefbc83"
            )
        }
    }
}