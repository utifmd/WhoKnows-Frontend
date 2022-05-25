package com.dudegenuine.whoknows.ui.compose.navigation.graph

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.dudegenuine.model.Resource
import com.dudegenuine.whoknows.ui.compose.component.misc.DialogSubscriber
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
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.ProfileEvent
import com.dudegenuine.whoknows.ui.vm.main.ActivityViewModel
import com.dudegenuine.whoknows.ui.vm.quiz.QuizViewModel
import com.dudegenuine.whoknows.ui.vm.result.ResultViewModel
import com.dudegenuine.whoknows.ui.vm.result.contract.IResultViewModel.Companion.RESULT_ROOM_ID_SAVED_KEY
import com.dudegenuine.whoknows.ui.vm.result.contract.IResultViewModel.Companion.RESULT_USER_ID_SAVED_KEY
import com.dudegenuine.whoknows.ui.vm.room.RoomViewModel
import com.dudegenuine.whoknows.ui.vm.user.UserViewModel
import com.dudegenuine.whoknows.ui.vm.user.contract.IUserViewModel.Companion.USER_ID_SAVED_KEY

/**
 * Tue, 25 Jan 2022
 * WhoKnows by utifmd
 **/
fun NavGraphBuilder.summaryGraph(props: IMainProps) {
    val profile = Screen.Home.Summary.RoomDetail.ProfileDetail
    val roomDetail = Screen.Home.Summary.RoomDetail

    val vmMain = props.vmMain as ActivityViewModel
    val isSignedIn = vmMain.isSignedIn

    composable(
        route = Screen.Home.Summary.RoomCreator.route) {
        val vmRoom: RoomViewModel = hiltViewModel()

        DialogSubscriber(vmMain, vmRoom)
        RoomCreatorScreen(
            viewModel = vmRoom,
            onBackPressed = props.router::popBackStack,
            onSucceed = {
                props.router.apply {
                    previousBackStackEntry?.savedStateHandle?.set(Resource.KEY_REFRESH, true)
                    popBackStack()
                }
            }
        )
    }

    composable(
        route = Screen.Home.Summary.RoomFinder.route){
        val vmRoom: RoomViewModel = hiltViewModel()

        DialogSubscriber(vmMain, vmRoom)
        RoomFinderScreen(
            viewModel = vmRoom,
            onBackPressed = props.router::popBackStack,
            onRoomSelected = { roomId ->
                props.router.navigate(
                    Screen.Home.Summary.RoomDetail.routeWithArgs(roomId, OWN_IS_FALSE))
            }
        )
    }

    composable(
        route = roomDetail.routeWithArgs("{$ROOM_ID_SAVED_KEY}", "{$ROOM_IS_OWN}"),
        deepLinks = if (isSignedIn) listOf( navDeepLink {
            uriPattern = roomDetail.uriWithArgs("{$ROOM_ID_SAVED_KEY}") }) else emptyList()){ entry ->
        val vmRoom: RoomViewModel = hiltViewModel()

        DialogSubscriber(vmMain, vmRoom)
        RoomDetail(
            viewModel = vmRoom,
            onBackPressed = {
                props.router.navigate(Screen.Home.route){
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            },
            isOwn = entry.arguments?.getString(ROOM_IS_OWN) == OWN_IS_TRUE,
            eventDetail = RoomEventDetail(props, vmRoom))
    }

    composable(
        route = Screen.Home.Summary.OnBoarding.routeWithArgs("{$ONBOARD_ROOM_ID_SAVED_KEY}")){
        val vmRoom: RoomViewModel = hiltViewModel()
        val event = object: IRoomEventBoarding {

            override fun onDoneResultPressed() {
                val screen = Screen.Home.Summary.route

                props.router.navigate(screen) {
                    popUpTo(screen) { inclusive = true }
                }
            }
        }

        DialogSubscriber(vmMain, vmRoom)
        RoomRoutedPreBoardingScreen(event, vmRoom)
    }

    composable(
        route = Screen.Home.Summary.RoomDetail.QuizCreator.routeWithArgs(
            "{$ROOM_ID_SAVED_KEY}", "{$ROOM_OWNER_SAVED_KEY}")) {
        val vmQuiz: QuizViewModel = hiltViewModel()

        DialogSubscriber(vmMain, vmQuiz)
        QuizCreatorScreen(
            viewModel = vmQuiz, onBackPressed = props.router::popBackStack) { quiz ->
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
            override fun onBackPressed() { props.router.popBackStack() } // override fun onDeletePressed() { props.router.popBackStack() }
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
        deepLinks = if (isSignedIn) listOf( navDeepLink {
            uriPattern = profile.uriWithArgs("{$USER_ID_SAVED_KEY}") }) else emptyList()){
        val vmUser: UserViewModel = hiltViewModel()

        DialogSubscriber(vmMain, vmUser)
        ProfileScreen(
            viewModel = vmUser,
            isOwn = false,
            event = ProfileEvent(props)
        )
    }

    composable(
        route = Screen.Home.Summary.RoomDetail.ResultDetail.routeWithArgs(
            "{$RESULT_ROOM_ID_SAVED_KEY}", "{$RESULT_USER_ID_SAVED_KEY}")){
        val vmResult: ResultViewModel = hiltViewModel()

        DialogSubscriber(vmMain, vmResult)
        ResultDetail(viewModel = vmResult, onBackPressed = props.router::popBackStack)
    }
}