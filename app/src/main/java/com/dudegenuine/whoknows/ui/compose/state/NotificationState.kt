package com.dudegenuine.whoknows.ui.compose.state

import androidx.compose.runtime.mutableStateOf
import com.dudegenuine.model.Notification
import java.util.*

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
sealed class NotificationState() {
    class FormState(): NotificationState() {
        val initialModel: Notification = mutableStateOf(Notification(
            "NTF-${UUID.randomUUID()}",
            "",
            "",
            "Just follow you",
            false,
            "",
            Date(),
            null,
            null)).value

        /*private val _badge = mutableStateOf(0)
        val badge = _badge.value

        fun onBadgeChange(fresh: Int){
            _badge.value = fresh
        }*/
    }
}
