package com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event

import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavHostController
import com.dudegenuine.whoknows.ui.compose.navigation.Screen

/**
 * Fri, 28 Jan 2022
 * WhoKnows by utifmd
 **/
@ExperimentalMaterialApi
class RoomEventDetail(
    private val router: NavHostController): IRoomEventDetail {

    override fun onNewRoomQuizPressed(roomId: String, owner: String) {
        router.navigate(Screen.Home.Summary.DetailRoomOwner.QuizCreator.withArgs(roomId, owner))
    }

    override fun onParticipantItemPressed() {
        router.navigate(Screen.Home.Setting.route)
    }
}