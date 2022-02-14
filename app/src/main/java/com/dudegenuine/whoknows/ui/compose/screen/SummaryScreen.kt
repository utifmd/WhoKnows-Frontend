package com.dudegenuine.whoknows.ui.compose.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.infrastructure.di.android.api.TimerNotificationService
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.RoomStatedPreBoardingScreen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEventBoarding
import com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event.IRoomEventHome

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun SummaryScreen(
    context: Context,
    eventHome: IRoomEventHome) {
    val service = Intent(context, TimerNotificationService::class.java)

    RoomStatedPreBoardingScreen(
        eventHome = eventHome,
        eventBoarding = object: IRoomEventBoarding {}){

        context.stopService(service)
    }
}