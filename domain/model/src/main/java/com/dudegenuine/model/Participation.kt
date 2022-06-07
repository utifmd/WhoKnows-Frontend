package com.dudegenuine.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Wed, 01 Jun 2022
 * WhoKnows by utifmd
 **/
data class Participation(
    val participantId: String,
    val user: User.Censored,
    val roomId: String,
    val userId: String,
    val roomTitle: String,
    val roomDesc: String,
    val roomMinute: Int,
    val pages: List<ParticipationPage>) {
    var currentQuestionIdx by mutableStateOf(0) //var duration: Double = (room.minute.toFloat() * 60).toDouble()
}
/*
sealed class Participation {
    object OnNothing: Participation()
    object OnLoading: Participation()
    data class OnBoarding(
        val participantId: String,
        val user: User.Censored,
        val roomId: String,
        val userId: String,
        val roomTitle: String,
        val roomDesc: String,
        val roomMinute: Int,
        val pages: List<ParticipationPage>): Participation() {
        var currentQuestionIdx by mutableStateOf(0) //var duration: Double = (room.minute.toFloat() * 60).toDouble()
    }
}*/
