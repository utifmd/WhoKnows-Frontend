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
        const val SUMMARY = "Summary"
        const val DISCOVER = "Discover"
        const val SETTING = "Settings"

        val listItem: (Int) -> Set<BottomDomain> = { fresh ->
            mutableSetOf(
                BottomDomain(
                    name = DISCOVER,
                    route = Screen.Home.Discover.route,
                    icon = Icons.Default.Explore,
                    badge = fresh
                ),
                BottomDomain(
                    name = SUMMARY,
                    route = Screen.Home.Summary.route,
                    icon = Icons.Default.Summarize
                ),
                BottomDomain(
                    name = SETTING,
                    route = Screen.Home.Setting.route,
                    icon = Icons.Default.Settings
                )
            )
        }
    }
}
