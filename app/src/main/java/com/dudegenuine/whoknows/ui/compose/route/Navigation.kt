package com.dudegenuine.whoknows.ui.compose.route

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.screen.DiscoverScreen
import com.dudegenuine.whoknows.ui.compose.screen.SettingScreen
import com.dudegenuine.whoknows.ui.compose.screen.SummaryScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomCreatorScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomHomeScreen

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
fun Navigation(router: NavHostController) {
    NavHost(
        navController = router,
        startDestination = Screen.SummaryScreen.route){

        composable(
            route = Screen.SummaryScreen.route) {
            SummaryScreen(router = router)
        }

        composable(
            route = Screen.DiscoverScreen.route) {
            DiscoverScreen(router = router)

            composable(
                route = Screen.DiscoverScreen.RoomHomeScreen.route) {
                RoomHomeScreen(router = router)
            }
            composable(
                route = Screen.DiscoverScreen.RoomCreatorScreen.route) {
                RoomCreatorScreen(router = router)
            }
            /*composable(Screen.DiscoverScreen.RoomFinderScreen.route) {
                RoomFinderScreen(router = router)
            }*/
        }

        composable(
            route = Screen.SettingScreen.route) {
            SettingScreen(router = router)
        }
    }
}