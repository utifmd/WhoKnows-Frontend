package com.dudegenuine.whoknows.ui.compose.navigation.graph

import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.dudegenuine.model.Resource.Companion.KEY_REFRESH
import com.dudegenuine.whoknows.ui.compose.component.misc.ImageViewer
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.DiscoverScreen
import com.dudegenuine.whoknows.ui.compose.screen.SettingScreen
import com.dudegenuine.whoknows.ui.compose.screen.SummaryScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.ProfileEvent
import com.dudegenuine.whoknows.ui.vm.file.IFileViewModel.Companion.PREVIEW_FILE_ID
import com.dudegenuine.whoknows.ui.vm.main.ActivityViewModel

/**
 * Wed, 19 Jan 2022
 * WhoKnows by utifmd
 **/
fun NavGraphBuilder.homeNavGraph(props: IMainProps) {
    val preview = Screen.Home.Preview
    val isSignedIn = (props.vmMain as ActivityViewModel).isSignedIn

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

            with(entry.savedStateHandle) {
                val isRefresh = getLiveData<Boolean>(KEY_REFRESH).observeAsState()

                if (isRefresh.value == true){
                    props.lazyPagingOwnerRooms.refresh()
                    props.lazyPagingRooms.refresh()

                    set<Boolean>(KEY_REFRESH, false)
                }
            }

            SummaryScreen(props)
        }

        settingGraph(props.router)
        composable(
            route = Screen.Home.Setting.route) {

            SettingScreen(
                event = ProfileEvent(props)
            )
        }

        composable(
            route = preview.routeWithArgs("{$PREVIEW_FILE_ID}"),
            deepLinks = if (isSignedIn) listOf( navDeepLink {
                uriPattern = preview.uriWithArgs("{$PREVIEW_FILE_ID}") }) else emptyList()) { entry ->

            ImageViewer(
                onBackPressed = {
                    props.router.navigate(Screen.Home.Setting.route){
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                fileId = entry.arguments?.getString(PREVIEW_FILE_ID) ?: "bacd3011-8aa5-4742-bf3f-be65ddefbc83"
            )
        }
    }
}