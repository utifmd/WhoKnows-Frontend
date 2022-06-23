package com.dudegenuine.whoknows.ux.compose.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavBackStackEntry
import com.dudegenuine.model.Resource
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IAppUseCaseModule.Companion.EMPTY_STRING
import com.dudegenuine.whoknows.ux.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ux.compose.screen.seperate.participation.ParticipationScreen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.room.RoomHomeScreen
import com.dudegenuine.whoknows.ux.compose.state.room.FlowParameter
import com.dudegenuine.whoknows.ux.vm.main.MainViewModel
import com.dudegenuine.whoknows.ux.vm.participation.ParticipationViewModel
import com.dudegenuine.whoknows.ux.vm.room.RoomViewModel

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun SummaryScreen(
    props: IMainProps,
    entry: NavBackStackEntry,
    roomViewModel: RoomViewModel,
    participationViewModel: ParticipationViewModel) {

    with(entry.savedStateHandle) {
        val isRefresh = getLiveData<Boolean>(Resource.KEY_REFRESH).observeAsState()

        when (isRefresh.value) {
            true -> {
                props.run {
                    lazyPagingRoomComplete.refresh()
                    lazyPagingRoomCensored.refresh()
                }
                set(Resource.KEY_REFRESH, false)
            }
            else -> {}
        }
    }
    // TODO: make sure participation works properly
    if (props.viewModel.state.participation != null) ParticipationScreen(
        viewModel = participationViewModel
    ) else RoomHomeScreen(
        viewModel = roomViewModel,
        props = props
    )
    LaunchedEffect(Unit/*props.viewModel.auth.user*/){
        with(props.viewModel as MainViewModel){
            onRoomCompleteParameterChange(FlowParameter.RoomComplete(auth.user?.id ?: EMPTY_STRING))
            onNotificationParameterChange(FlowParameter.Notification(auth.user?.id ?: EMPTY_STRING))
        }
    }
}