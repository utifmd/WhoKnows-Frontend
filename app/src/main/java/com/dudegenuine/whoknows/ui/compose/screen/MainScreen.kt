package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.component.GeneralBottomBar
import com.dudegenuine.whoknows.ui.compose.model.BottomItem
import com.dudegenuine.whoknows.ui.compose.route.Navigation
import com.dudegenuine.whoknows.ui.compose.route.Screen

/**
 * Sat, 15 Jan 2022
 * WhoKnows by utifmd
 **/

@Composable
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
fun MainScreen(
    navController: NavHostController){

    Scaffold(
        bottomBar = {
            GeneralBottomBar(
                items = bottomBarItems,
                controller = navController,
                onItemClick = { navController.navigate(it.route) })}) {

        Navigation(
            router = navController
        )
    }
}

val bottomBarItems: List<BottomItem> = listOf(
    BottomItem(
        name = BottomItem.SUMMARY,
        route = Screen.SummaryScreen.route,
        icon = Icons.Default.Summarize
    ),

    BottomItem(
        name = BottomItem.DISCOVER,
        route = Screen.DiscoverScreen.route,
        icon = Icons.Default.Explore //, badge = 2
    ),

    BottomItem(
        name = BottomItem.SETTING,
        route = Screen.SettingScreen.route,
        icon = Icons.Default.Settings
    )
)