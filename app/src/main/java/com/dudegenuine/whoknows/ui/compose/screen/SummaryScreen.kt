package com.dudegenuine.whoknows.ui.compose.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomStatedPreBoardingScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEventBoarding
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEventHome

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun SummaryScreen(
    //context: Context,
    eventHome: IRoomEventHome) {
    //val service = Intent(context, TimerService::class.java)

    RoomStatedPreBoardingScreen(
        eventHome = eventHome,
        eventBoarding = object: IRoomEventBoarding { }){

        //context.stopService(service)
    }
}