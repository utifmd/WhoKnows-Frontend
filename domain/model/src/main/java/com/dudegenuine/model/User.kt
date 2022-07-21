package com.dudegenuine.model

import android.util.Log
import com.dudegenuine.model.common.Utility
import java.util.*

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
sealed class User {
    data class Signer(
        val payload: String,
        val password: String,
        val token: String,
    )
    data class Censored(
        val userId: String,
        val fullName: String,
        val username: String,
        val profileUrl: String,
        var isCurrentUser: Boolean,
        var tokens: List<String>
    )
    data class Complete (
        val id: String,
        var fullName: String,
        var email: String,
        var phone: String,
        var username: String,
        var password: String,
        var profileUrl: String,
        var isCurrentUser: Boolean,
        var createdAt: Date,
        var updatedAt: Date?,
        var tokens: List<String>,
        var participants: List<Participant>,
        var rooms: List<Room.Censored>,
        var notifications: List<Notification>){

        val badge get() = notifications
            .filter{ it.recipientIds.isEmpty() }.count{ !it.seen }

        val sortedParticipants
            get() = participants.sortedByDescending{ it.createdAt }

        val completeParticipants
            get() = participants.filter{ it.expired }

        val isPropsBlank: Boolean =
            /*fullName.isBlank() ||*/ email.isBlank() ||// phone.isBlank() ||
                username.isBlank() || password.isBlank()

        var exactPassword = password

        init {
            try { exactPassword = Utility.decrypt(password) }
            catch (e: Exception){ Log.d("Model User TAG", e.localizedMessage ?: "") }
        }

        companion object {
            const val PAYLOAD = "payload"
            const val PASSWORD = "password"
        }
    }
}
