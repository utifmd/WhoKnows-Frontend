package com.dudegenuine.whoknows.ui.compose.state

/**
 * Mon, 20 Dec 2021
 * WhoKnows by utifmd
 **/
/*sealed class RoomState {
    object CurrentRoom: RoomState() //{ var currentUserId by mutableStateOf("") }

    data class BoardingQuiz(
        val participantId: String,
        val participantName: String,
        val roomId: String,
        val roomTitle: String,
        val roomDesc: String,
        val roomMinute: Int,
        val quizzes: List<OnBoardingState>): RoomState(){
            var currentQuestionIdx by mutableStateOf(0) //var duration: Double = (room.minute.toFloat() * 60).toDouble()
    }

    data class BoardingResult(
        val title: String,
        val data: Result?): RoomState()

    class FormState: RoomState() {
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
                minute.text.isNotBlank()
            ).value

        val model: Room
            get() = mutableStateOf(Room(
                id = "ROM-${UUID.randomUUID()}",
                userId = "",
                minute = if(minute.text.isBlank()) 0 else minute.text.toInt(),
                title = title.text,
                description = desc.text,
                expired = false,
                questions = emptyList(),
                participants = emptyList(),
                createdAt = Date(),
                updatedAt = null )).value

        private val _participantModel = mutableStateOf(
            Participant(
                id = "PPN-${UUID.randomUUID()}",
                roomId = "",
                userId = "",
                currentPage = "0",
                timeLeft = null,
                expired = false,
                createdAt = Date(),
                updatedAt = null,
                user = null
            )
        )

        val participantModel: Participant
            get() = _participantModel.value

        val isGetValid: Boolean
            get() = mutableStateOf(roomId.isNotBlank()).value

        fun onTitleChange (it: String){
            _title.value = TextFieldValue(text = it)
        }

        fun onDescChange (it: String){
            _desc.value = TextFieldValue(text = it)
        }

        fun onMinuteChange (it: String){
            _minute.value = TextFieldValue(text = it)
        }

        fun onRoomIdChange(id: String) {
            _roomId.value = id
        }

        fun onTimerChange(time: Double){
            _timer.value = time
        }
    }

    @Stable
    class OnBoardingState(
        val quiz: Quiz,
        val questionIndex: Int,
        val totalQuestionsCount: Int,
        val showPrevious: Boolean,
        val showDone: Boolean) {
            var enableNext by mutableStateOf(false)
            var answer by mutableStateOf<Answer?>(null)
            var isCorrect by mutableStateOf(false)
    }
}*/

