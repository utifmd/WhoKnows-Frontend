package com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event

import androidx.navigation.NavHostController
import com.dudegenuine.whoknows.ui.compose.navigation.Screen

/**
 * Wed, 26 Jan 2022
 * WhoKnows by utifmd
 **/
class RoomEvent(
    private val router: NavHostController): IRoomEvent {

    override fun onNewClassPressed() {
        router.navigate(Screen.Home.Summary.RoomCreator.route)
    }

    override fun onJoinWithACodePressed() {
        router.navigate(Screen.Home.Summary.RoomFinder.route)
    }

    override fun onRoomItemSelected(id: String) {
        router.navigate(Screen.Home.Summary.DetailRoomOwner.withArgs(id))
    }
}