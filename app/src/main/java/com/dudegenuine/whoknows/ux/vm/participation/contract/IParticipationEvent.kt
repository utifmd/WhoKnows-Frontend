package com.dudegenuine.whoknows.ux.vm.participation.contract

import android.content.BroadcastReceiver
import com.dudegenuine.model.*

/**
 * Fri, 03 Jun 2022
 * WhoKnows by utifmd
 **/
interface IParticipationEvent {
    fun onTimerReceiver(participation: Participation): BroadcastReceiver

    fun onBoardingActionPressed(index: Int, type: Quiz.Action.Type)
    fun onBoardingBackPressed()
    fun onBoardingPrevPressed()
    fun onBoardingNextPressed()
    fun onBoardingDonePressed()
    fun onResultDismissPressed()
    //fun onBackRoomBoardingPressed()

    fun onPreResult(participation: Participation)
    fun getRoom(roomId: String, onSucceed: (Room.Complete) -> Unit)
    fun getCurrentUser(onSucceed: (User.Complete) -> Unit)
    fun onTimerChange(time: Double)
    fun onResult(
        roomTitle: String, result: Result, notification: Notification,
        register: Messaging.GroupAdder, pusher: Messaging.Pusher
    )
}