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
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent.Companion.ROOM_ID_SAVED_KEY
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent.Companion.ROOM_IS_OWN
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent.Companion.ROOM_OWNER_SAVED_KEY
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent.Companion.ROOM_OWN_IS_FALSE
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent.Companion.ROOM_OWN_IS_TRUE
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
                val route = Screen.Home.Summary.route

                router.navigate(route){
                    popUpTo(route){ inclusive = true }
                }
            }
        )
    }

    composable(
        route = Screen.Home.Summary.RoomFinder.route){
        RoomFinderScreen(
            modifier = modifier){ roomId ->

            router.navigate(Screen.Home.Summary.DetailRoomOwner.withArgs(roomId, ROOM_OWN_IS_FALSE))
        }
    }

    composable(
        route = Screen.Home.Summary.DetailRoomOwner.withArgs(
            "{$ROOM_ID_SAVED_KEY}", "{$ROOM_IS_OWN}")){ entry ->
        RoomDetail(
            modifier = modifier,
            isOwn = entry.arguments?.getString(ROOM_IS_OWN) == ROOM_OWN_IS_TRUE,
            roomEventDetail = RoomEventDetail(router = router)
        )
    }

    composable(
        route = Screen.Home.Summary.DetailRoomOwner.QuizCreator.withArgs(
            "{$ROOM_ID_SAVED_KEY}", "{$ROOM_OWNER_SAVED_KEY}")){

        QuizCreatorScreen { quiz ->
            val route = Screen.Home.Summary.DetailRoomOwner.withArgs(
                quiz.roomId, ROOM_OWN_IS_TRUE)

            router.navigate(route){
                popUpTo(route){ inclusive = true }
            }
        }
    }
}