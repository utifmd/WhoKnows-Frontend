package com.dudegenuine.whoknows.ui.compose.navigation

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.Room
import com.dudegenuine.model.User
import com.dudegenuine.whoknows.ui.compose.navigation.graph.authNavGraph
import com.dudegenuine.whoknows.ui.compose.navigation.graph.homeNavGraph
import com.dudegenuine.whoknows.ui.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ui.vm.main.ActivityViewModel
import com.dudegenuine.whoknows.ui.vm.main.IActivityViewModel
import com.dudegenuine.whoknows.ui.vm.quiz.QuizViewModel
import com.dudegenuine.whoknows.ui.vm.room.RoomViewModel
import com.dudegenuine.whoknows.ui.vm.room.contract.IRoomViewModel
import com.dudegenuine.whoknows.ui.vm.user.UserViewModel
import com.dudegenuine.whoknows.ui.vm.user.contract.IUserViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * Wed, 19 Jan 2022
 * WhoKnows by utifmd
 **/
@ExperimentalCoroutinesApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
@FlowPreview
@Composable
fun MainNavigation(
    controller: NavHostController, // initialPassed: String,
    destination: String,
    vmMain: ActivityViewModel = hiltViewModel(),
    vmQuiz: QuizViewModel = hiltViewModel(),
    vmRoom: RoomViewModel = hiltViewModel(),
    vmUser: UserViewModel = hiltViewModel()) {

    val props = object: IMainProps {
        override val context: Context = LocalContext.current
        override val router: NavHostController = controller
        override val vmMain: IActivityViewModel = vmMain
        override val vmUser: IUserViewModel = vmUser
        override val vmRoom: IRoomViewModel = vmRoom

        override val roomsPager: LazyPagingItems<Room.Censored> =
            vmRoom.rooms.collectAsLazyPagingItems()

        override val ownerRoomsPager: LazyPagingItems<Room.Complete> =
            vmRoom.roomsOwner.collectAsLazyPagingItems()

        override val participantsPager: LazyPagingItems<User.Censored> =
            vmUser.participants.collectAsLazyPagingItems()

        override val quizzesPager: LazyPagingItems<Quiz.Complete> =
            vmQuiz.questions.collectAsLazyPagingItems()

    }

    NavHost(
        route = Screen.ROOT_ROUTE,
        startDestination = destination,
        navController = controller) {

        authNavGraph(props)
        homeNavGraph(props) /*, initial = if (initialPassed == MESSAGE_INTENT *//*TIME_RUNNING*//*) Screen.Home.Discover else Screen.Home.Summary)*/
    }
}