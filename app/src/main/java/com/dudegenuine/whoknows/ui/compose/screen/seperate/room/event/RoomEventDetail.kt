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
        router.navigate(
            route = Screen.Home.Summary.RoomDetail.QuizCreator.withArgs(roomId, owner))
    }

    override fun onBoardingRoomPressed(roomId: String) {
        val screen = Screen.Home.Summary.OnBoarding.withArgs(/*participantId, */roomId)

        with (router) {
            repeat(3) { popBackStack() }
            navigate(screen)
        }
    }

    override fun onParticipantItemPressed(userId: String) {
        router.navigate(
            route = Screen.Home.Summary.RoomDetail.ProfileDetail.withArgs(userId))
    }

    override fun onQuestionItemPressed(quizId: String) {
        router.navigate(
            route = Screen.Home.Summary.RoomDetail.QuizDetail.withArgs(quizId))
    }

    override fun onDeleteRoomSucceed(roomId: String) {
        router.navigate(Screen.Home.Summary.route){
            popUpTo(Screen.Home.Summary.route){
                inclusive = true
            }
        }
    }
}