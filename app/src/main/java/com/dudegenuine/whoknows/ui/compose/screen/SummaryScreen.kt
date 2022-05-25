package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.runtime.Composable
import com.dudegenuine.whoknows.ui.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomStatedPreBoardingScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEventBoarding
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.RoomEventHome
import com.dudegenuine.whoknows.ui.vm.room.RoomViewModel

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun SummaryScreen(props: IMainProps, viewModel: RoomViewModel) {

    RoomStatedPreBoardingScreen(
        props = props,
        viewModel = viewModel,
        eventHome = RoomEventHome(props.router),
        eventBoarding = object: IRoomEventBoarding { },
    )
}