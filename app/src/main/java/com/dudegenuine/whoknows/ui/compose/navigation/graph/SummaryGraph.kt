package com.dudegenuine.whoknows.ui.compose.navigation.graph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.infrastructure.common.Constants.BASE_CLIENT_URL
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
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
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEvent
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
fun NavGraphBuilder.summaryGraph(
    router: NavHostController) {
    //context: Context, //val service = Intent(context, TimerService::class.java)

    composable(
        route = Screen.Home.Summary.RoomCreator.route) {
        RoomCreatorScreen(
            onBackPressed = router::popBackStack,
            onSucceed = {
                val route = Screen.Home.Summary.route

                router.navigate(route){
                    popUpTo(route) { inclusive = true }
                }
            }
        )
    }

    composable(
        route = Screen.Home.Summary.RoomFinder.route){
        RoomFinderScreen(
            onBackPressed = router::popBackStack,
            onRoomSelected = { roomId ->
                router.navigate(
                    Screen.Home.Summary.RoomDetail.withArgs(roomId, OWN_IS_FALSE))
            }
        )
    }

    composable(
        route = Screen.Home.Summary.RoomDetail.withArgs("{$ROOM_ID_SAVED_KEY}", "{$ROOM_IS_OWN}"),
        deepLinks = listOf(navDeepLink { uriPattern = "$BASE_CLIENT_URL/who-knows/room/{$ROOM_ID_SAVED_KEY}" })){ entry ->

        RoomDetail(
            onBackPressed = router::popBackStack,
            isOwn = entry.arguments?.getString(ROOM_IS_OWN) == OWN_IS_TRUE,
            eventRouter = RoomEventDetail(router = router)) /*{ time ->

            service.putExtra(ITimerService.INITIAL_TIME_KEY, time)
                .apply(context::startService)
        }*/
    }

    composable(
        route = Screen.Home.Summary.OnBoarding.withArgs("{${IRoomEvent.ONBOARD_ROOM_ID_SAVED_KEY}}")){
        val event = object: IRoomEventBoarding {

            override fun onDoneResultPressed() {
                val screen = Screen.Home.Summary.route

                with (router) {
                    //popBackStack()

                    navigate(screen) {
                        popUpTo(screen) { inclusive = true }
                    }
                }
            }
        }

        RoomRoutedPreBoardingScreen(event)/*{
            context.stopService(service)
        }*/
    }

    composable(
        route = Screen.Home.Summary.RoomDetail.QuizCreator.withArgs(
            "{$ROOM_ID_SAVED_KEY}", "{$ROOM_OWNER_SAVED_KEY}")){

        QuizCreatorScreen(onBackPressed = router::popBackStack) { quiz ->
            val screen = Screen.Home.Summary.RoomDetail.withArgs(
                quiz.roomId, OWN_IS_TRUE)

            with (router) {
                repeat (2){ popBackStack() }

                navigate(screen)

                /*navigate(screen) {
                    popUpTo(screen) { inclusive = true }
                }*/
            }
        }
    }

    composable(
        route = Screen.Home.Summary.RoomDetail.QuizDetail.withArgs(
            "{$QUIZ_ID_SAVED_KEY}")){

        val event = object: IQuizPublicEvent {
            override fun onBackPressed() { router.popBackStack() }
            override fun onDeletePressed() { router.popBackStack() }
            override fun onPicturePressed(fileId: String) {
                router.navigate(Screen.Home.Preview.withArgs(fileId))
            }
        }

        QuizScreen(
            stateCompose = object: IQuizPublicState {
                override val event: IQuizPublicEvent = event
            }
        )
    }

    composable(
        route = Screen.Home.Summary.RoomDetail.ProfileDetail.withArgs("{$USER_ID_SAVED_KEY}"),
        deepLinks = listOf(navDeepLink { uriPattern = "$BASE_CLIENT_URL/who-knows/user/{$USER_ID_SAVED_KEY}" })){

        val event = object: IProfileEvent {
            override fun onBackPressed() { router.popBackStack() }
            override fun onPicturePressed(fileId: String) {
                router.navigate(Screen.Home.Preview.withArgs(fileId))
            }
        }

        ProfileScreen(
            isOwn = false,
            event = event
        )
    }

    composable(
        route = Screen.Home.Summary.RoomDetail.ResultDetail.withArgs(
            "{$RESULT_ROOM_ID_SAVED_KEY}", "{$RESULT_USER_ID_SAVED_KEY}")){

        ResultDetail(onBackPressed = router::popBackStack)
    }
}