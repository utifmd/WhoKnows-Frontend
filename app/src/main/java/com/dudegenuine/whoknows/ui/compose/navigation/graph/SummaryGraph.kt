package com.dudegenuine.whoknows.ui.compose.navigation.graph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.QuizCreatorScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.QuizScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.contract.IQuizPublicEvent
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.contract.IQuizPublicState
import com.dudegenuine.whoknows.ui.compose.screen.seperate.quiz.contract.IQuizState.Companion.QUIZ_ID_SAVED_KEY
import com.dudegenuine.whoknows.ui.compose.screen.seperate.result.ResultDetail
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomCreatorScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomDetail
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomFinderScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomRoutedPreBoardingScreen
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
import com.dudegenuine.whoknows.ui.vm.result.contract.IResultViewModel.Companion.RESULT_ROOM_ID_SAVED_KEY
import com.dudegenuine.whoknows.ui.vm.result.contract.IResultViewModel.Companion.RESULT_USER_ID_SAVED_KEY
import com.dudegenuine.whoknows.ui.vm.user.contract.IUserViewModel.Companion.USER_ID_SAVED_KEY
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * Tue, 25 Jan 2022
 * WhoKnows by utifmd
 **/
@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
@ExperimentalFoundationApi
fun NavGraphBuilder.summaryGraph(props: IMainProps) {
    val profile = Screen.Home.Summary.RoomDetail.ProfileDetail
    val roomDetail = Screen.Home.Summary.RoomDetail

    composable(
        route = Screen.Home.Summary.RoomCreator.route) {
        RoomCreatorScreen(
            onBackPressed = props.router::popBackStack,
            onSucceed = {
                val route = Screen.Home.Summary.route

                props.router.navigate(route){
                    popUpTo(route) { inclusive = true }
                }
            }
        )
    }

    composable(
        route = Screen.Home.Summary.RoomFinder.route){
        RoomFinderScreen(
            onBackPressed = props.router::popBackStack,
            onRoomSelected = { roomId ->
                props.router.navigate(
                    Screen.Home.Summary.RoomDetail.routeWithArgs(roomId, OWN_IS_FALSE))
            }
        )
    }

    composable(
        route = roomDetail.routeWithArgs("{$ROOM_ID_SAVED_KEY}", "{$ROOM_IS_OWN}"),
        deepLinks = listOf( navDeepLink {
            uriPattern = roomDetail.uriWithArgs("{$ROOM_ID_SAVED_KEY}") })){ entry ->

        RoomDetail(
            onBackPressed = props.router::popBackStack,
            isOwn = entry.arguments?.getString(ROOM_IS_OWN) == OWN_IS_TRUE,
            eventDetail = RoomEventDetail(props))
    }

    composable(
        route = Screen.Home.Summary.OnBoarding.routeWithArgs("{$ONBOARD_ROOM_ID_SAVED_KEY}")){
        val event = object: IRoomEventBoarding {

            override fun onDoneResultPressed() {
                val screen = Screen.Home.Summary.route

                props.router.navigate(screen) {
                    popUpTo(screen) { inclusive = true }
                }
            }
        }

        RoomRoutedPreBoardingScreen(event)
    }

    composable(
        route = Screen.Home.Summary.RoomDetail.QuizCreator.routeWithArgs(
            "{$ROOM_ID_SAVED_KEY}", "{$ROOM_OWNER_SAVED_KEY}")) {

        QuizCreatorScreen(onBackPressed = props.router::popBackStack) { quiz ->
            val screen = Screen.Home.Summary.RoomDetail.routeWithArgs(
                quiz.roomId, OWN_IS_TRUE)

            with (props.router) {
                repeat (2){ popBackStack() }

                navigate(screen)
            }
        }
    }

    composable(
        route = Screen.Home.Summary.RoomDetail.QuizDetail.routeWithArgs(
            "{$QUIZ_ID_SAVED_KEY}")){

        val event = object: IQuizPublicEvent {
            override fun onBackPressed() { props.router.popBackStack() }
            override fun onDeletePressed() { props.router.popBackStack() }
            override fun onPicturePressed(fileId: String?) {
                if (fileId.isNullOrBlank()) return

                props.router.navigate(Screen.Home.Preview.routeWithArgs(fileId))
            }
        }

        QuizScreen(
            stateCompose = object: IQuizPublicState {
                override val event: IQuizPublicEvent = event
            }
        )
    }

    composable(
        route = profile.routeWithArgs("{$USER_ID_SAVED_KEY}"),
        deepLinks = listOf( navDeepLink {
            uriPattern = profile.uriWithArgs("{$USER_ID_SAVED_KEY}") })){

        val event = object: IProfileEvent {
            override fun onBackPressed() { props.router.popBackStack() }
            override fun onPicturePressed(fileId: String?) {
                if(fileId.isNullOrBlank()) return

                props.router.navigate(Screen.Home.Preview.routeWithArgs(fileId))
            }
        }

        ProfileScreen(
            isOwn = false,
            event = event
        )
    }

    composable(
        route = Screen.Home.Summary.RoomDetail.ResultDetail.routeWithArgs(
            "{$RESULT_ROOM_ID_SAVED_KEY}", "{$RESULT_USER_ID_SAVED_KEY}")){

        ResultDetail(onBackPressed = props.router::popBackStack)
    }
}