package com.dudegenuine.whoknows.ui.compose.route

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dudegenuine.whoknows.ui.compose.model.BtmNavItem.Companion.DISCOVER
import com.dudegenuine.whoknows.ui.compose.model.BtmNavItem.Companion.SETTING
import com.dudegenuine.whoknows.ui.compose.model.BtmNavItem.Companion.SUMMARY
import com.dudegenuine.whoknows.ui.compose.screen.DiscoverScreen
import com.dudegenuine.whoknows.ui.compose.screen.SettingScreen
import com.dudegenuine.whoknows.ui.compose.screen.SummaryScreen

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun Navigation(controller: NavHostController) {
    NavHost(navController = controller, startDestination = SUMMARY.lowercase()){
        composable(SUMMARY.lowercase()) { SummaryScreen() }
        composable(DISCOVER.lowercase()) { DiscoverScreen() }
        composable(SETTING.lowercase()) { SettingScreen() }
    }
}