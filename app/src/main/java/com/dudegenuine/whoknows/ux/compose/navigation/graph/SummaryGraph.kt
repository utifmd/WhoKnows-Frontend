package com.dudegenuine.whoknows.ux.compose.navigation.graph

import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.dudegenuine.model.Resource
import com.dudegenuine.whoknows.ux.compose.component.misc.LoggingSubscriber
import com.dudegenuine.whoknows.ux.compose.navigation.Screen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ux.compose.screen.seperate.participation.ParticipationScreen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.quiz.QuizCreatorScreen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.quiz.QuizScreen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.quiz.contract.IQuizPublicEvent
import com.dudegenuine.whoknows.ux.compose.screen.seperate.result.ResultDetail
import com.dudegenuine.whoknows.ux.compose.screen.seperate.room.RoomCreatorScreen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.room.RoomDetail
import com.dudegenuine.whoknows.ux.compose.screen.seperate.room.RoomFinderScreen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.user.ProfileScreen
import com.dudegenuine.whoknows.ux.vm.main.MainViewModel
import com.dudegenuine.whoknows.ux.vm.participation.ParticipationViewModel
import com.dudegenuine.whoknows.ux.vm.quiz.QuizViewModel
import com.dudegenuine.whoknows.ux.vm.quiz.contract.IQuizPublicState
import com.dudegenuine.whoknows.ux.vm.quiz.contract.IQuizState.Companion.QUIZ_ID_SAVED_KEY
import com.dudegenuine.whoknows.ux.vm.result.ResultViewModel
import com.dudegenuine.whoknows.ux.vm.result.contract.IResultViewModel.Companion.RESULT_ROOM_ID_SAVED_KEY
import com.dudegenuine.whoknows.ux.vm.result.contract.IResultViewModel.Companion.RESULT_USER_ID_SAVED_KEY
import com.dudegenuine.whoknows.ux.vm.room.RoomViewModel
import com.dudegenuine.whoknows.ux.vm.room.contract.IRoomEvent.Companion.ROOM_ID_SAVED_KEY
import com.dudegenuine.whoknows.ux.vm.room.contract.IRoomEvent.Companion.ROOM_OWNER_SAVED_KEY
import com.dudegenuine.whoknows.ux.vm.room.contract.IRoomViewModel.Companion.KEY_PARTICIPATION_ROOM_ID
import com.dudegenuine.whoknows.ux.vm.user.UserViewModel
import com.dudegenuine.whoknows.ux.vm.user.contract.IUserViewModel.Companion.USER_ID_SAVED_KEY

/**
 * Tue, 25 Jan 2022
 * WhoKnows by utifmd
 **/
fun NavGraphBuilder.summaryGraph(props: IMainProps) {
    val profile = Screen.Home.Summary.RoomDetail.ProfileDetail
    val roomDetail = Screen.Home.Summary.RoomDetail

    val vmMain = props.viewModel as MainViewModel
    val isLoggedIn = vmMain.isLoggedInByPrefs

    composable(
        route = Screen.Home.Summary.RoomCreator.route){
        val vmRoom: RoomViewModel = hiltViewModel()

        LoggingSubscriber(vmMain, vmRoom)
        RoomCreatorScreen(viewModel = vmRoom)
    }
    composable(
        route = Screen.Home.Summary.RoomFinder.route){
        val vmRoom: RoomViewModel = hiltViewModel()

        LoggingSubscriber(vmMain, vmRoom)
        RoomFinderScreen(viewModel = vmRoom)
    }
    composable(
        route = roomDetail.routeWithArgs("{$ROOM_ID_SAVED_KEY}"/*, "{$ROOM_IS_OWN}"*/),
        deepLinks = if (isLoggedIn) listOf( navDeepLink{
            uriPattern = roomDetail.uriWithArgs("{$ROOM_ID_SAVED_KEY}") }) else emptyList()){ entry ->
        val isRefresh = entry.savedStateHandle.getLiveData<Boolean>(Resource.KEY_REFRESH).observeAsState()
        val vmRoom: RoomViewModel = hiltViewModel()
        val (isRefreshAfter, setIsRefreshAfter) = remember{ mutableStateOf(false) }
        if (isRefresh.value == true){
            val roomId = entry.arguments?.getString(ROOM_ID_SAVED_KEY)

            setIsRefreshAfter(true)
            roomId?.let(vmRoom::getRoom)
            entry.savedStateHandle[Resource.KEY_REFRESH] = false
        }
        LoggingSubscriber(vmMain, vmRoom)
        RoomDetail(viewModel = vmRoom, onBackPressed =
            if(isRefreshAfter) vmRoom::onNavigateBackThenRefresh else vmRoom::onBackPressed
        )
    }
    composable(
        route = Screen.Home.Summary.Participation.routeWithArgs("{${KEY_PARTICIPATION_ROOM_ID}}")){
        val vmParticipation: ParticipationViewModel = hiltViewModel()

        LoggingSubscriber(vmMain, vmParticipation)
        ParticipationScreen(viewModel = vmParticipation)
    }

    composable(
        route = Screen.Home.Summary.RoomDetail.QuizCreator.routeWithArgs(
            "{$ROOM_ID_SAVED_KEY}", "{$ROOM_OWNER_SAVED_KEY}")) {
        val vmQuiz: QuizViewModel = hiltViewModel()

        LoggingSubscriber(vmMain, vmQuiz)
        QuizCreatorScreen(
            viewModel = vmQuiz/*, onBackPressed = props.router::popBackStack*/) /*{ quiz ->
            val screen = Screen.Home.Summary.RoomDetail.routeWithArgs(
                quiz.roomId, OWN_IS_TRUE)

            with (props.router) {
                repeat (2){ popBackStack() }

                navigate(screen)
            }
        }*/
    }

    composable(
        route = Screen.Home.Summary.RoomDetail.QuizDetail.routeWithArgs(
            "{$QUIZ_ID_SAVED_KEY}")){ //backStackEntry ->

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
        deepLinks = if (isLoggedIn) listOf( navDeepLink {
            uriPattern = profile.uriWithArgs("{$USER_ID_SAVED_KEY}") }) else emptyList()){
        val vmUser: UserViewModel = hiltViewModel()

        LoggingSubscriber(vmMain, vmUser)
        ProfileScreen(
            viewModel = vmUser,
            state = vmUser.state
        )
    }

    composable(
        route = Screen.Home.Summary.RoomDetail.ResultDetail.routeWithArgs(
            "{$RESULT_ROOM_ID_SAVED_KEY}", "{$RESULT_USER_ID_SAVED_KEY}")){
        val vmResult: ResultViewModel = hiltViewModel()

        LoggingSubscriber(vmMain, vmResult)
        ResultDetail(viewModel = vmResult, onBackPressed = props.router::popBackStack)
    }
}