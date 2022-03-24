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
            route = Screen.Home.Summary.RoomDetail.QuizCreator.routeWithArgs(roomId, owner))
    }

    override fun onBoardingRoomPressed(roomId: String) {
        val screen = Screen.Home.Summary.OnBoarding.routeWithArgs(/*participantId, */roomId)

        with (router) {
            repeat(3) { popBackStack() }
            navigate(screen)
        }
    }

    override fun onParticipantItemPressed(userId: String) {
        router.navigate(
            route = Screen.Home.Summary.RoomDetail.ProfileDetail.routeWithArgs(userId))
    }

    override fun onResultPressed(roomId: String, userId: String) {
        router.navigate(
            route = Screen.Home.Summary.RoomDetail.ResultDetail.routeWithArgs(roomId, userId)
        )
    }

    override fun onQuestionItemPressed(quizId: String) {
        router.navigate(
            route = Screen.Home.Summary.RoomDetail.QuizDetail.routeWithArgs(quizId))
    }

    override fun onDeleteRoomSucceed(roomId: String) {
        router.navigate(Screen.Home.Summary.route){
            popUpTo(Screen.Home.Summary.route){
                inclusive = true
            }
        }
    }

    override fun onRoomRetailPressed(roomId: String) {
        router.apply {
            popBackStack()
            navigate(Screen.Home.Summary.RoomDetail.routeWithArgs(
                roomId, IRoomEvent.OWN_IS_TRUE)
            )
        }
    }
}