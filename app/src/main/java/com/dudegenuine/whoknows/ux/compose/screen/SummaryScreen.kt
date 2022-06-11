package com.dudegenuine.whoknows.ux.compose.screen

import androidx.compose.runtime.Composable
import com.dudegenuine.whoknows.ux.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ux.compose.screen.seperate.participation.ParticipationScreen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.room.RoomHomeScreen
import com.dudegenuine.whoknows.ux.vm.participation.ParticipationViewModel
import com.dudegenuine.whoknows.ux.vm.room.RoomViewModel

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun SummaryScreen(
    props: IMainProps,
    roomViewModel: RoomViewModel,
    participationViewModel: ParticipationViewModel) {

    if (props.viewModel.state.participation != null) ParticipationScreen(
        viewModel = participationViewModel
    ) else RoomHomeScreen(
        viewModel = roomViewModel,
        props = props
    )
}