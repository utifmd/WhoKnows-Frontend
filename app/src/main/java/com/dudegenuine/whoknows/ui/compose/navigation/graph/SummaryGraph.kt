package com.dudegenuine.whoknows.ui.compose.navigation.graph

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.QuizCreatorScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomCreatorScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomDetail
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomFinderScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.RoomEventDetail

/**
 * Tue, 25 Jan 2022
 * WhoKnows by utifmd
 **/
@ExperimentalMaterialApi
@ExperimentalFoundationApi
fun NavGraphBuilder.summaryGraph(
    modifier: Modifier = Modifier,
    router: NavHostController){

    composable(
        route = Screen.Home.Summary.RoomCreator.route){
        RoomCreatorScreen(
            modifier = modifier,
            onSucceed = {
                router.navigate(Screen.Home.Summary.route){
                    popUpTo(Screen.Home.Summary.route){ inclusive = true }
                }
            }
        )
    }

    composable(
        route = Screen.Home.Summary.RoomFinder.route){
        RoomFinderScreen(
            modifier = modifier
        )
    }

    composable(
        route = Screen.Home.Summary.DetailRoomOwner.withArgs("{${IRoomEvent.SAVED_KEY_ROOM}}")){
        RoomDetail(
            modifier = modifier,
            isOwn = true,
            roomEventDetail = RoomEventDetail(router = router)
        )
    }

    composable(
        route = Screen.Home.Summary.DetailRoomOwner.QuizCreator.route){

        QuizCreatorScreen(
            modifier = modifier
        )
    }
}