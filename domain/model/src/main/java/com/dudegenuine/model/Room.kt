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
        val user: User.Censored?,
        val questionSize: Int,
        val isOwner: Boolean = false,

        var impressions: List<Impression>,
        var impressionSize: Int,
        var impressed: Boolean = false,
        var hasImpressedBefore: Boolean = false,
        var impression: Impression?,

        val participantSize: Int): Room()
    
    data class Complete(
        val id: String,
        val userId: String,
        var minute: Int,
        val title: String,
        val token: String,
        val description: String,
        var isOwner: Boolean = false, //
        var isJoinAccepted: Boolean = false, //
        var isParticipated: Boolean = false, //
        var isParticipant: Boolean = false, //
        var expired: Boolean,
        var private: Boolean, //
        var createdAt: Date,
        var updatedAt: Date?,

        var impressions: List<Impression> = emptyList(),
        var impressionSize: Int,
        var impressed: Boolean = false,
        var hasImpressedBefore: Boolean = false,
        var impression: Impression? = null,

        val user: User.Censored?,
        var questions: List<Quiz.Complete>,
        var participants: List<Participant>): Room() { // use that expired participant as an complete participation and display
        val isPropsBlank: Boolean =
            minute == 0 || title.isBlank() || userId.isBlank() || description.isBlank()

        val sortedParticipants
            get() = participants
                .sortedBy{ isOwner }
                .sortedBy{ it.expired }
                .sortedByDescending{ it.createdAt }

        val completeParticipants
            get() = participants.filter{ it.expired }
    }
}
