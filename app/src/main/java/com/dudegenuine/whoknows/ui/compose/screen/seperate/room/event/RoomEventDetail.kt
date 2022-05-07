package com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event

import com.dudegenuine.model.Participant
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.Room
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ui.compose.state.DialogState
import com.dudegenuine.whoknows.ui.vm.main.ActivityViewModel
import com.dudegenuine.whoknows.ui.vm.room.RoomViewModel

/**
 * Fri, 28 Jan 2022
 * WhoKnows by utifmd
 **/
class RoomEventDetail(
    private val props: IMainProps,
    private val vmRoom: RoomViewModel): IRoomEventDetail {
    private val vmMain = props.vmMain as ActivityViewModel

    override fun onNewRoomQuizPressed(room: Room.Complete) {
        if (room.participants.isNotEmpty()) vmMain
            .onShowSnackBar(props.context.getString(R.string.already_participantion))
        else props.router.navigate(
            route = Screen.Home.Summary.RoomDetail.QuizCreator.routeWithArgs(room.id, room.userId))
    }

    override fun onBoardingRoomPressed(roomId: String) {
        val screen = Screen.Home.Summary.OnBoarding.routeWithArgs(/*participantId, */roomId)

        with (props.router) {
            repeat(3) { popBackStack() }
            navigate(screen)
        }
    }

    override fun onParticipantItemPressed(userId: String) {
        props.router.navigate(
            route = Screen.Home.Summary.RoomDetail.ProfileDetail.routeWithArgs(userId))
    }

    override fun onResultPressed(roomId: String, userId: String) {
        props.router.navigate(
            route = Screen.Home.Summary.RoomDetail.ResultDetail.routeWithArgs(roomId, userId)
        )
    }

    override fun onQuestionItemPressed(quizId: String) {
        props.router.navigate(
            route = Screen.Home.Summary.RoomDetail.QuizDetail.routeWithArgs(quizId))
    }

    override fun onBackPressed() {
        props.router.popBackStack()
        //vmRoom.roomsOwner.retry()
    }

    private fun onDeleteRoomSucceed() {
        props.router.navigate(Screen.Home.Summary.route){
            popUpTo(Screen.Home.Summary.route){ inclusive = true }
        }
    }

    private fun onReNavigateRoom(roomId: String) {
        props.router.apply {
            popBackStack()
            navigate(
                Screen.Home.Summary.RoomDetail.routeWithArgs(roomId, IRoomEvent.OWN_IS_TRUE))
        }
    }

    override fun onShareRoomPressed(room: Room.Complete) {
        if(room.questions.size >= 3) vmRoom.onSharePressed(room.id)
        else vmMain.onShowSnackBar(props.context.getString(R.string.allowed_after_add_3_quest))
    }

    override fun onSetCopyRoomPressed(room: Room.Complete) {
        if(room.questions.size >= 3) vmRoom.onCopyRoomIdPressed(room.id)
        else vmMain.onShowSnackBar(props.context.getString(R.string.allowed_after_add_3_quest))
    }

    override fun onJoinRoomDirectlyPressed(room: Room.Complete) {
        val disclaimer = when {
            vmRoom.currentUserId.isBlank() -> props.context.getString(R.string.not_signed_in_yet_to_join)
            vmRoom.currentUserId == room.userId -> props.context.getString(R.string.the_owner_itself_to_join)
            room.participants.any { it.userId == vmRoom.currentUserId } -> props.context.getString(R.string.already_joined_to_join)
            else -> null }
        val accepted = vmRoom.currentUserId.isNotBlank() && vmRoom.currentUserId != room.userId &&
                room.participants.all { it.userId != vmRoom.currentUserId }
        val dialog = DialogState(props.context.getString(R.string.participate_the_class), disclaimer,
            onSubmitted = if (accepted) {
                { onBoardingRoomPressed(room.id) }} else null)
        vmMain.onDialogStateChange(dialog)
    }

    override fun onCloseRoomPressed(room: Room.Complete, onComplete: () -> Unit) {
        val disclaimer = props.context.getString(R.string.no_longer_participation)
        val dialog = DialogState(props.context.getString(R.string.close_the_class), disclaimer) {
            if(room.questions.size >= 3) vmRoom.expireRoom(room, onComplete)
            else vmMain.onShowSnackBar(props.context.getString(R.string.allowed_after_add_3_quest)) }
        vmMain.onDialogStateChange(dialog)
    }

    override fun onDeleteRoomPressed(room: Room.Complete) {
        val disclaimer = when {
            room.participants.isNotEmpty() -> props.context.getString(R.string.already_participantion)
            room.questions.any { it.images.isNotEmpty() } -> props.context.getString(R.string.there_is_no_quest)
            else -> null
        }
        val accepted = room.participants.isEmpty() && room.questions.all { it.images.isEmpty() }
        val dialog = DialogState(props.context.getString(R.string.delete_class), disclaimer,
            onSubmitted = if (accepted) {
                { vmRoom.onDeleteRoomPressed(room.id, ::onDeleteRoomSucceed) }} else null)
        vmMain.onDialogStateChange(dialog)
    }

    override fun onParticipantLongPressed(enabled: Boolean, participant: Participant) {
        val dialog = DialogState(props.context.getString(R.string.delete_participant),
            if (!enabled) props.context.getString(R.string.allowed_when_class_opened) else null,
            onSubmitted = if(enabled) {
                { vmRoom.onDeleteParticipantPressed(participant, ::onReNavigateRoom) }} else null)
        vmMain.onDialogStateChange(dialog)
    }

    override fun onQuestionLongPressed(room: Room.Complete, quiz: Quiz.Complete) {
        val accepted = !room.expired && room.participants.isEmpty()
        val disclaimer: String? = if(room.expired) props.context.getString(R.string.allowed_when_class_opened)
            else if(room.participants.isNotEmpty()) props.context.getString(R.string.already_participantion) else null
        val dialog = DialogState(props.context.getString(R.string.delete_question), disclaimer,
            onSubmitted = if(accepted) {
                { vmRoom.onDeleteQuestionPressed(quiz, ::onReNavigateRoom) }} else null)
        vmMain.onDialogStateChange(dialog)
    }
}