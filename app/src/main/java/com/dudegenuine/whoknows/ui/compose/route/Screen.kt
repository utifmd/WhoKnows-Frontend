package com.dudegenuine.whoknows.ui.compose.route

import com.dudegenuine.model.common.ImageUtil.strOf

/**
 * Mon, 17 Jan 2022
 * WhoKnows by utifmd
 **/
sealed class Screen(val route: String){
    object SummaryScreen: Screen(strOf<SummaryScreen>())
    object DiscoverScreen: Screen(strOf<DiscoverScreen>()){
        object RoomHomeScreen: Screen(strOf<RoomHomeScreen>())
        object RoomCreatorScreen: Screen(strOf<RoomCreatorScreen>())
        object RoomFinderScreen: Screen(strOf<RoomFinderScreen>())
    }
    object SettingScreen: Screen(strOf<SettingScreen>()){
        object SettingEditScreen: Screen(strOf<SettingEditScreen>())
    }
}
