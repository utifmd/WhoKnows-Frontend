package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.runtime.Composable
import com.dudegenuine.whoknows.ui.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomStatedPreBoardingScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEventBoarding
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.RoomEventHome

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun SummaryScreen(props: IMainProps) {

    RoomStatedPreBoardingScreen(
        props = props,
        eventHome = RoomEventHome(props.router),
        eventBoarding = object: IRoomEventBoarding { },
    )
}