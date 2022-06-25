package com.dudegenuine.whoknows.ux.compose.navigation.graph

import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.dudegenuine.model.Resource
import com.dudegenuine.whoknows.ux.compose.component.misc.ImageViewer
import com.dudegenuine.whoknows.ux.compose.component.misc.LoggingSubscriber
import com.dudegenuine.whoknows.ux.compose.navigation.Screen
import com.dudegenuine.whoknows.ux.compose.screen.DiscoverScreen
import com.dudegenuine.whoknows.ux.compose.screen.SettingScreen
import com.dudegenuine.whoknows.ux.compose.screen.SummaryScreen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ux.compose.state.ResourceState
import com.dudegenuine.whoknows.ux.vm.file.FileViewModel
import com.dudegenuine.whoknows.ux.vm.file.IFileViewModel.Companion.PREVIEW_FILE_ID
import com.dudegenuine.whoknows.ux.vm.main.MainViewModel
import com.dudegenuine.whoknows.ux.vm.room.RoomViewModel
import com.dudegenuine.whoknows.ux.vm.room.contract.IRoomViewModel.Companion.KEY_PARTICIPATION_ROOM_ID
import com.dudegenuine.whoknows.ux.vm.user.UserViewModel

/**
 * Wed, 19 Jan 2022
 * WhoKnows by utifmd
 **/
fun NavGraphBuilder.homeNavGraph(props: IMainProps) {
    val preview = Screen.Home.Preview
    val vmMain = props.viewModel as MainViewModel
    val isLoggedIn = vmMain.isLoggedIn
    val isParticipated = vmMain.isParticipated.isNotBlank()
    /*
        val participatedRoute = Screen.Home.Summary.Participation.routeWithArgs(vmMain.isParticipated)
        val option = NavOptions.Builder().setPopUpTo(Screen.Home.Summary.route, true).build()
        vmMain.onScreenStateChange(ScreenState.Navigate.To(participatedRoute, option))
    * */
    navigation(
        route = Screen.Home.route,
        startDestination = if(isParticipated) Screen.Home.Summary.Participation.routeWithArgs("{$KEY_PARTICIPATION_ROOM_ID}")
            else Screen.Home.Summary.route) {

        discoverGraph(props)
        composable(
            route = Screen.Home.Discover.route) {

            DiscoverScreen(props)
        }
        summaryGraph(props)
        composable(route = Screen.Home.Summary.route) { entry ->
            val vmRoom: RoomViewModel = hiltViewModel()
            val refreshState = entry
                .savedStateHandle
                .getLiveData<Boolean>(Resource.KEY_REFRESH)
                .observeAsState()

            if (refreshState.value == true) {
                props.lazyPagingRoomComplete.refresh()
                props.lazyPagingRoomCensored.refresh()
                entry.savedStateHandle[Resource.KEY_REFRESH] = false
            }
            LoggingSubscriber(vmMain, vmRoom)
            SummaryScreen(props, vmRoom)
        }
        settingGraph(props)
        composable(
            route = Screen.Home.Setting.route) {
            val vmUser: UserViewModel = hiltViewModel()

            LoggingSubscriber(vmMain, vmUser)
            SettingScreen(
                viewModel = vmUser,
                state = ResourceState(
                    user = vmMain.auth.user?.apply {
                        isCurrentUser = true
                    }
                )
            )
        }
        composable(
            route = preview.routeWithArgs("{$PREVIEW_FILE_ID}"),
            deepLinks = if (isLoggedIn) listOf( navDeepLink {
                uriPattern = preview.uriWithArgs("{$PREVIEW_FILE_ID}") }) else emptyList()) { entry ->
            val vmFile: FileViewModel = hiltViewModel()

            LoggingSubscriber(vmMain, vmFile)
            ImageViewer(
                viewModel = vmFile,
                onBackPressed = props.router::popBackStack,
                fileId = entry.arguments?.getString(PREVIEW_FILE_ID) ?: "bacd3011-8aa5-4742-bf3f-be65ddefbc83"
            )
        }
    }
}