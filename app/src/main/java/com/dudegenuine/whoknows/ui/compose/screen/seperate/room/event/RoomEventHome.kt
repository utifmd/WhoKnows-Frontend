package com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event

import androidx.navigation.NavHostController
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent.Companion.OWN_IS_TRUE

/**
 * Wed, 26 Jan 2022
 * WhoKnows by utifmd
 **/
class RoomEventHome(
    private val router: NavHostController): IRoomEventHome {

    override fun onNewClassPressed() {
        router.navigate(Screen.Home.Summary.RoomCreator.route)
    }

    override fun onJoinRoomWithACodePressed() {
        router.navigate(Screen.Home.Summary.RoomFinder.route)
    }

    override fun onRoomItemSelected(id: String) {
        router.navigate(Screen.Home.Summary.RoomDetail.routeWithArgs(id, OWN_IS_TRUE))
    }
}