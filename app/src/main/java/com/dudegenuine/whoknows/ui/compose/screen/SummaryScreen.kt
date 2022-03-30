package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomStatedPreBoardingScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEventBoarding
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.RoomEventHome
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun SummaryScreen(props: IMainProps) {
    //context: Context,
    //val lazyPagingRooms = viewModel.roomsOwner.collectAsLazyPagingItems()
//    lazyPagingRooms: LazyPagingItems<Room.Complete>,
//    eventHome: IRoomEventHome) {
    //val service = Intent(context, TimerService::class.java)

    RoomStatedPreBoardingScreen(
        props = props,
        eventHome = RoomEventHome(props.router),
        eventBoarding = object: IRoomEventBoarding { })/*{

        //context.stopService(service)
    }*/
}