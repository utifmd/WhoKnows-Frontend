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
        val password: String
    )
    data class Censored(
        val userId: String,
        val fullName: String,
        val username: String,
        val profileUrl: String,
    )
    data class Complete (
        val id: String,
        var fullName: String,
        var email: String,
        var phone: String,
        var username: String,
        var password: String,
        var profileUrl: String,
        var createdAt: Date,
        var updatedAt: Date?,
        var participants: List<Participant>,
        var rooms: List<Room.Censored>,
        var notifications: List<Notification>){

        val sortedParticipants
            get() = participants.sortedByDescending { it.createdAt }

        val isPropsBlank: Boolean =
            /*fullName.isBlank() ||*/ email.isBlank() ||// phone.isBlank() ||
                username.isBlank() || password.isBlank()

        var exactPassword = password

        init {
            try {
                exactPassword = Utility.decrypt(password)
                Log.d("Model User TAG", ": $exactPassword")
            } catch (e: Exception){
                Log.d("Model User TAG", e.localizedMessage ?: "")
            }
        }

        companion object {
            const val PAYLOAD = "payload"
            const val PASSWORD = "password"
        }
    }
}
