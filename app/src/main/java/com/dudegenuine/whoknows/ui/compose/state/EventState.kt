package com.dudegenuine.whoknows.ui.compose.state

/**
 * Wed, 13 Apr 2022
 * WhoKnows by utifmd
 **/
sealed class EventState(val data: Any){
    class SnackBar(data: String): EventState(data)
    class Dialog(data: DialogState): EventState(data)
}