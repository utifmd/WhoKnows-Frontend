package com.dudegenuine.model

import java.util.*

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
sealed class Room {
    data class Censored(
        val roomId: String,
        val userId: String,
        val minute: Int,
        val title: String,
        val token: String,
        val description: String, //val to: String,
        val expired: Boolean,
        val private: Boolean,
        val usernameOwner: String,
        val fullNameOwner: String,
        val questionSize: Int,
        val impressionSize: Int,
        val isOwner: Boolean = false,
        val impressed: Boolean = false,
        val participantSize: Int): Room()
    
    data class Complete(
        val id: String,
        val userId: String,
        var minute: Int,
        val title: String,
        val token: String,
        val description: String,
        var isOwner: Boolean = false, //
        var expired: Boolean,
        var private: Boolean, //
        var createdAt: Date,
        var updatedAt: Date?,
        val impressionSize: Int, //
        val impressed: Boolean = false, //
        val user: User.Censored?,
        var questions: List<Quiz.Complete>,
        var participants: List<Participant>): Room() { // use that expired participant as an complete participation and display
        val isPropsBlank: Boolean =
            minute == 0 || title.isBlank() || userId.isBlank() || description.isBlank()
    
    }
}
