package com.dudegenuine.whoknows.ux.compose.state

import androidx.compose.runtime.mutableStateOf
import com.dudegenuine.model.Notification
import java.util.*

/**
 * Thu, 10 Feb 2022
 * WhoKnows by utifmd
 **/
sealed class NotificationState() {
    class FormState: NotificationState() {
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
    }
}
