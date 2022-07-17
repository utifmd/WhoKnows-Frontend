package com.dudegenuine.whoknows.ux.compose.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.dudegenuine.whoknows.ux.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ux.compose.screen.seperate.room.RoomHomeScreen
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

    LaunchedEffect(Unit){
        with(props.viewModel as MainViewModel){
            auth.user?.id?.let(::onUserIndicatorChange)
        }
    }
}