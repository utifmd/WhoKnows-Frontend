package com.dudegenuine.whoknows.ux.compose.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IAppUseCaseModule.Companion.EMPTY_STRING
import com.dudegenuine.whoknows.ux.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ux.compose.screen.seperate.room.RoomHomeScreen
import com.dudegenuine.whoknows.ux.compose.state.room.FlowParameter
import com.dudegenuine.whoknows.ux.vm.main.MainViewModel
import com.dudegenuine.whoknows.ux.vm.room.RoomViewModel

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun SummaryScreen(
    props: IMainProps,
    roomViewModel: RoomViewModel) {
    RoomHomeScreen(viewModel = roomViewModel, props = props)

    LaunchedEffect(Unit/*props.viewModel.auth.user*/){
        with(props.viewModel as MainViewModel){
            onRoomCompleteParameterChange(FlowParameter.RoomComplete(auth.user?.id ?: EMPTY_STRING))
            onNotificationParameterChange(FlowParameter.Notification(auth.user?.id ?: EMPTY_STRING))
        }
    }
}