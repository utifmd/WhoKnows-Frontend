package com.dudegenuine.whoknows.ux.compose.state

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import com.dudegenuine.model.Room
import com.dudegenuine.whoknows.BuildConfig.BASE_CLIENT_URL
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IAppUseCaseModule.Companion.EMPTY_STRING
import java.util.*

class RoomState {

    private val _roomId = mutableStateOf("")
    val roomId: String
        get() = _roomId.value

    private val _title = mutableStateOf(TextFieldValue(text = ""))
    val title: TextFieldValue
        get() = _title.value

    private val _desc = mutableStateOf(TextFieldValue(text = ""))
    val desc: TextFieldValue
        get() = _desc.value

    private val _timer = mutableStateOf(0.0)
    val timer: Double
        get() = _timer.value

    private val _minute = mutableStateOf(TextFieldValue(text = ""))
    val minute: TextFieldValue
        get() = _minute.value

    val isPostValid: Boolean
        get() = mutableStateOf(
            title.text.isNotBlank() &&
                    desc.text.isNotBlank() &&
                    minute.text.isNotBlank()).value

    val room: Room.Complete
        get() = mutableStateOf(
            Room.Complete(
                id = "ROM-${UUID.randomUUID()}",
                userId = EMPTY_STRING,
                minute = if (minute.text.isBlank()) 0 else minute.text.toInt(),
                title = title.text,
                token = EMPTY_STRING,
                description = desc.text,
                expired = false,
                questions = emptyList(),
                participants = emptyList(),
                createdAt = Date(),
                updatedAt = null,
                user = null,
                private = false,
                impressed = false,
                isOwner = false,
                impressionSize = 0
            )
        ).value
    /*val notification: Notification
        get() = mutableStateOf(Notification(
            notificationId = "NTF-${UUID.randomUUID()}",
            userId = "", roomId = "", event = "", seen = false,
            recipientId = "", createdAt = Date(), null, null)).value*/
    /*val result: Result = mutableStateOf(Result(
        resultId = "RSL-${UUID.randomUUID()}",
        roomId = "", *//*participantId = "",*//* userId = "", correctQuiz = emptyList(), wrongQuiz = emptyList(), score = 0,
        createdAt = Date(),
        updatedAt = null, user = null)).value*/

    /*private val _participantModel = mutableStateOf(
        Participant(
            id = "PPN-${UUID.randomUUID()}",
            roomId = "", userId = "", currentPage = "0", timeLeft = null, expired = false,
            createdAt = Date(), updatedAt = null, user = null, isCurrentUser = false)
    )*/

    fun onTitleChange (it: String){
        _title.value = TextFieldValue(text = it)
    }

    fun onDescChange (it: String){
        if (it.length <= 250) _desc.value = TextFieldValue(text = it)
    }

    fun onMinuteChange (it: String){
        _minute.value = TextFieldValue(text = it)
    }

    fun onRoomIdChange(id: String) {
        var fresh = id
        if (id.contains(BASE_CLIENT_URL)){
            fresh = id.substringAfterLast('/')
        }
        _roomId.value = fresh
    }

    companion object {
        val room: Room.Complete
            get() = mutableStateOf(
                Room.Complete(
                    id = "ROM-${UUID.randomUUID()}",
                    userId = EMPTY_STRING,
                    minute = 10,
                    title = EMPTY_STRING,
                    token = EMPTY_STRING,
                    description = EMPTY_STRING,
                    expired = false,
                    questions = emptyList(),
                    participants = emptyList(),
                    createdAt = Date(),
                    updatedAt = null,
                    user = null,
                    private = false,
                    impressed = false,
                    isOwner = false,
                    impressionSize = 0
                )
            ).value
    }
}