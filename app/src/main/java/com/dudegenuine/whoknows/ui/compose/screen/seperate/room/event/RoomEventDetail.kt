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
            route = Screen.Home.Summary.DetailRoomOwner.QuizCreator.withArgs(roomId, owner))
    }

    override fun onParticipantItemPressed(userId: String) {
        // if (isOwn)
        /*if (user != null)
        router.navigate(
            route = Screen.Home.Setting.route)*/
    }

    override fun onQuestionItemPressed(quizId: String) {
        router.navigate(
            route = Screen.Home.Summary.DetailRoomOwner.DetailQuiz.withArgs(quizId))
    }

    override fun onDeleteRoomSucceed(roomId: String) {
        router.navigate(Screen.Home.Summary.route){
            popUpTo(Screen.Home.Summary.route){
                inclusive = true
            }
        }
    }
}