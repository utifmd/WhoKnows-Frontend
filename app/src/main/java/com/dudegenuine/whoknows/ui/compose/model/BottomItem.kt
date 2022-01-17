package com.dudegenuine.whoknows.ui.compose.model

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
data class BottomItem (
    val name: String,
    val route: String,
    val icon: ImageVector,
    val badge: Int = 0){

    companion object {
        const val SUMMARY = "Summary"
        const val DISCOVER = "Discover"
        const val SETTING = "Settings"
    }
}
