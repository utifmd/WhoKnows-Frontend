package com.dudegenuine.whoknows.ux.compose.navigation.graph

import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.dudegenuine.model.Resource.Companion.KEY_REFRESH
import com.dudegenuine.model.Resource.Companion.KEY_USER_ID
import com.dudegenuine.whoknows.ux.compose.component.misc.ImageViewer
import com.dudegenuine.whoknows.ux.compose.component.misc.LoggingSubscriber
import com.dudegenuine.whoknows.ux.compose.navigation.Screen
import com.dudegenuine.whoknows.ux.compose.screen.DiscoverScreen
import com.dudegenuine.whoknows.ux.compose.screen.SettingScreen
import com.dudegenuine.whoknows.ux.compose.screen.SummaryScreen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ux.compose.state.ResourceState
import com.dudegenuine.whoknows.ux.compose.state.room.FlowParameter
import com.dudegenuine.whoknows.ux.vm.file.FileViewModel
import com.dudegenuine.whoknows.ux.vm.file.IFileViewModel.Companion.PREVIEW_FILE_ID
import com.dudegenuine.whoknows.ux.vm.main.MainViewModel
import com.dudegenuine.whoknows.ux.vm.participation.ParticipationViewModel
import com.dudegenuine.whoknows.ux.vm.room.RoomViewModel
import com.dudegenuine.whoknows.ux.vm.user.UserViewModel

/**
 * Wed, 19 Jan 2022
 * WhoKnows by utifmd
 **/
fun NavGraphBuilder.homeNavGraph(props: IMainProps) {
    val preview = Screen.Home.Preview
    val vmMain = props.viewModel as MainViewModel
    val isLoggedIn = vmMain.isLoggedInByPrefs

    navigation(
        route = Screen.Home.route,
        startDestination = Screen.Home.Summary.route) {

        discoverGraph(props)
        composable(
            route = Screen.Home.Discover.route) {

            DiscoverScreen(props)
        }
        summaryGraph(props)
        composable(
            route = Screen.Home.Summary.route) { entry ->
            val vmParticipation: ParticipationViewModel = hiltViewModel()
            val vmRoom: RoomViewModel = hiltViewModel()

            with(entry.savedStateHandle) {
                val isRefresh = getLiveData<Boolean>(KEY_REFRESH).observeAsState()
                val currentUserId = getLiveData<String>(KEY_USER_ID).observeAsState()

                when{
                    isRefresh.value == true -> {
                        props.run {
                            lazyPagingRoomCensored.refresh()
                            lazyPagingRoomComplete.refresh()
                        }
                        set(KEY_REFRESH, false)
                    }
                    currentUserId.value?.isNotBlank() == true -> currentUserId.value?.let {
                        vmMain.onRoomCompleteParameterChange(FlowParameter.RoomComplete(it))
                        vmMain.onNotificationParameterChange(FlowParameter.Notification(it))
                    }
                    else -> {}
                }
            }
            LoggingSubscriber(vmMain, vmRoom)
            LoggingSubscriber(vmMain, vmParticipation)
            SummaryScreen(props, vmRoom, vmParticipation)
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