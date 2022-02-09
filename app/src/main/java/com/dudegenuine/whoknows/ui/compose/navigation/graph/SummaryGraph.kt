package com.dudegenuine.whoknows.ui.compose.navigation.graph

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.QuizCreatorScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.QuizScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.contract.IQuizPublicEvent
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.contract.IQuizPublicState
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.contract.IQuizState.Companion.QUIZ_ID_SAVED_KEY
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomCreatorScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomDetail
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomFinderScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomOnboardScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent.Companion.ONBOARD_ROOM_ID_SAVED_KEY
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent.Companion.OWN_IS_FALSE
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent.Companion.OWN_IS_TRUE
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent.Companion.ROOM_ID_SAVED_KEY
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent.Companion.ROOM_IS_OWN
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent.Companion.ROOM_OWNER_SAVED_KEY
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEventBoarding
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.RoomEventDetail
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.ProfileScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.IProfileEvent
import com.dudegenuine.whoknows.ui.presenter.user.contract.IUserViewModel.Companion.USER_ID_SAVED_KEY

/**
 * Tue, 25 Jan 2022
 * WhoKnows by utifmd
 **/
@ExperimentalMaterialApi
@ExperimentalCoilApi
@ExperimentalFoundationApi
fun NavGraphBuilder.summaryGraph(
    router: NavHostController){

    composable(
        route = Screen.Home.Summary.RoomCreator.route){
        RoomCreatorScreen(
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
        RoomFinderScreen { roomId ->

            router.navigate(Screen.Home.Summary.RoomDetail.withArgs(roomId, OWN_IS_FALSE))
        }
    }

    composable(
        route = Screen.Home.Summary.RoomDetail.withArgs(
            "{$ROOM_ID_SAVED_KEY}", "{$ROOM_IS_OWN}")){ entry ->
        RoomDetail(
            isOwn = entry.arguments?.getString(ROOM_IS_OWN) == OWN_IS_TRUE,
            roomEventDetail = RoomEventDetail(router = router)
        )
    }

    composable(
        route = Screen.Home.Summary.RoomDetail.QuizCreator.withArgs(
            "{$ROOM_ID_SAVED_KEY}", "{$ROOM_OWNER_SAVED_KEY}")){

        QuizCreatorScreen { quiz ->
            val route = Screen.Home.Summary.RoomDetail.withArgs(
                quiz.roomId, OWN_IS_TRUE)

            router.apply {
                popBackStack()
                popBackStack()

                navigate(route)
            }
        }
    }

    composable(
        route = Screen.Home.Summary.RoomDetail.QuizDetail.withArgs(
            "{$QUIZ_ID_SAVED_KEY}")){

        val event = object: IQuizPublicEvent {
            override fun onPicturePressed(url: String) {
                Log.d("onPicturePressed", "triggered")
            }
        }

        QuizScreen(
            stateCompose = object: IQuizPublicState {
                override val event: IQuizPublicEvent = event
            }
        )
    }

    composable(
        route = Screen.Home.Summary.RoomDetail.ProfileDetail.withArgs(
            "{$USER_ID_SAVED_KEY}")){

        val event = object: IProfileEvent {

        }

        ProfileScreen(
            isOwn = false,
            event = event
        )
    }

    composable(
        route = Screen.Home.Summary.OnBoarding.withArgs(
            /*"{$ONBOARD_PPN_ID_SAVED_KEY}", */"{$ONBOARD_ROOM_ID_SAVED_KEY}")){

        RoomOnboardScreen(
            event = object: IRoomEventBoarding {
                override fun onDonePressed() {
                    /*router.navigate(
                        route = Screen.Home.Summary.OnBoarding.Result.route
                    )*/
                }
            }
        )
    }
}