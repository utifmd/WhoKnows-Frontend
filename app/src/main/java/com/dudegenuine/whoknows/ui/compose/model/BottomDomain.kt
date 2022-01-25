package com.dudegenuine.whoknows.ui.compose.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.ui.graphics.vector.ImageVector
import com.dudegenuine.whoknows.ui.compose.navigation.Screen

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
data class BottomDomain (
    val name: String,
    val route: String,
    val icon: ImageVector,
    val badge: Int = 0){

    companion object {
        private const val SUMMARY = "Summary"
        private const val DISCOVER = "Discover"
        private const val SETTING = "Settings"

        val list: List<BottomDomain> get() = listOf(
            BottomDomain(
                name = SUMMARY,
                route = Screen.Home.Summary.route,
                icon = Icons.Default.Summarize
            ),

            BottomDomain(
                name = DISCOVER,
                route = Screen.Home.Discover.route,
                icon = Icons.Default.Explore //, badge = 2
            ),

            BottomDomain(
                name = SETTING,
                route = Screen.Home.Setting.route,
                icon = Icons.Default.Settings
            )
        )
    }
}
