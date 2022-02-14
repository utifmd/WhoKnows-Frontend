package com.dudegenuine.whoknows.ui.compose.navigation.graph

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.DiscoverScreen
import com.dudegenuine.whoknows.ui.compose.screen.SettingScreen
import com.dudegenuine.whoknows.ui.compose.screen.SummaryScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.RoomEventHome
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.ProfileEvent
import com.dudegenuine.whoknows.ui.presenter.user.UserViewModel

/**
 * Wed, 19 Jan 2022
 * WhoKnows by utifmd
 **/
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
fun NavGraphBuilder.homeNavGraph(
    modifier: Modifier = Modifier,
    context: Context,
    initial: Screen,
    router: NavHostController,
    viewModel: UserViewModel) {

    navigation(
        route = Screen.Home.route,
        startDestination = initial.route) {

        composable(
            route = Screen.Home.Summary.route) {

            SummaryScreen(
                context = context,
                eventHome = RoomEventHome(router)
            )
        }

        composable(
            route = Screen.Home.Discover.route) {

            DiscoverScreen(
                router = router
            )
        }

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
    }
}