package com.dudegenuine.whoknows.ui.compose.navigation.graph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.component.misc.ImageViewer
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.DiscoverScreen
import com.dudegenuine.whoknows.ui.compose.screen.SettingScreen
import com.dudegenuine.whoknows.ui.compose.screen.SummaryScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.RoomEventHome
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.ProfileEvent
import com.dudegenuine.whoknows.ui.vm.file.IFileViewModel.Companion.PREVIEW_FILE_ID
import com.dudegenuine.whoknows.ui.vm.user.UserViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * Wed, 19 Jan 2022
 * WhoKnows by utifmd
 **/
@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
fun NavGraphBuilder.homeNavGraph(
    router: NavHostController,
    modifier: Modifier = Modifier, //initial: Screen,
    viewModel: UserViewModel) {

    navigation(
        route = Screen.Home.route,
        startDestination = Screen.Home.Discover.route) {

        discoverGraph(router)
        composable(
            route = Screen.Home.Discover.route) {

            DiscoverScreen(
                router = router
            )
        }

        summaryGraph(router)
        composable(
            route = Screen.Home.Summary.route) {

            SummaryScreen(
                eventHome = RoomEventHome(router)
            )
        }

        settingGraph(router)
        composable(
            route = Screen.Home.Setting.route) {

            SettingScreen(
                modifier = modifier,
                event = ProfileEvent(
                    onSignOutClicked = viewModel::signOutUser,
                    router = router,
                )
            )
        }

        val preview = Screen.Home.Preview
        composable(
            route = preview.routeWithArgs("{$PREVIEW_FILE_ID}"),
            deepLinks = listOf( navDeepLink {
                uriPattern = preview.uriWithArgs("{$PREVIEW_FILE_ID}") })) { entry ->

            ImageViewer(
                onBackPressed = router::popBackStack,
                fileId = entry.arguments?.getString(PREVIEW_FILE_ID) ?: "bacd3011-8aa5-4742-bf3f-be65ddefbc83"
            )
        }
    }
}