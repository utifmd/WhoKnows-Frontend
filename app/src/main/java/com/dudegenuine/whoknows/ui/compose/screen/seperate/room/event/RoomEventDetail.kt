package com.dudegenuine.whoknows.ui.compose.screen.seperate.room.event

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.model.Participant
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.Room
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ui.compose.state.DialogState
import com.dudegenuine.whoknows.ui.vm.main.ActivityViewModel
import com.dudegenuine.whoknows.ui.vm.room.RoomViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * Fri, 28 Jan 2022
 * WhoKnows by utifmd
 **/
@ExperimentalCoilApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
class RoomEventDetail(
    private val props: IMainProps): IRoomEventDetail {
    private val vmMain = props.vmMain as ActivityViewModel
    private val vmRoom = props.vmRoom as RoomViewModel

    override fun onNewRoomQuizPressed(roomId: String, owner: String) {
        props.router.navigate(
            route = Screen.Home.Summary.RoomDetail.QuizCreator.routeWithArgs(roomId, owner))
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
    }

    override fun onDeleteRoomSucceed() {
        props.router.navigate(Screen.Home.Summary.route){
            popUpTo(Screen.Home.Summary.route){ inclusive = true }
        }
    }

    /*override fun onRoomDetailPressed(roomId: String) {
        props.router.apply {
            popBackStack()
            navigate(Screen.Home.Summary.RoomDetail.routeWithArgs(
                roomId, IRoomEvent.OWN_IS_TRUE)
            )
        }
    }*/

    override fun onShareRoomPressed(room: Room.Complete) {
        if(room.questions.size >= 3) vmRoom.onSharePressed(room.id)
        else vmMain.onShowSnackBar(props.context.getString(R.string.allowed_after_add_3_quest))
    }

    override fun onJoinRoomDirectlyPressed(room: Room.Complete) {
        val disclaimer = when {
            vmRoom.currentUserId.isBlank() -> props.context.getString(R.string.not_signed_in_yet_to_join)
            vmRoom.currentUserId == room.userId -> props.context.getString(R.string.the_owner_itself_to_join)
            room.participants.any { it.userId == vmRoom.currentUserId } -> props.context.getString(R.string.already_joined_to_join)
            else -> null }
        val accepted = vmRoom.currentUserId.isNotBlank() && vmRoom.currentUserId != room.userId &&
                room.participants.any { it.userId != vmRoom.currentUserId }
        val dialog = DialogState(props.context.getString(R.string.participate_the_class), disclaimer,
            onSubmitted = if (accepted) {{ onBoardingRoomPressed(room.id) }} else null)
        vmMain.onDialogStateChange(dialog)
    }

    override fun onParticipantLongPressed(enabled: Boolean, participant: Participant) {
        val dialog = DialogState(props.context.getString(R.string.delete_participant),
            onSubmitted = if(enabled) {{ vmRoom.onDeleteParticipantPressed(participant) }} else null)
        vmMain.onDialogStateChange(dialog)
    }

    override fun onQuestionLongPressed(enabled: Boolean, quiz: Quiz.Complete, roomId: String) {
        val dialog = DialogState(props.context.getString(R.string.delete_question),
            onSubmitted = if(enabled) {{ vmRoom.onDeleteQuestionPressed(quiz) }} else null)
        vmMain.onDialogStateChange(dialog)
    }

    override fun onCloseRoomPressed(room: Room.Complete) {
        val dialog = DialogState(props.context.getString(R.string.close_the_class)) {
            if(room.questions.size >= 3) vmRoom.expireRoom(room)
            else vmMain.onShowSnackBar(props.context.getString(R.string.allowed_after_add_3_quest)) }
        vmMain.onDialogStateChange(dialog)
    }

    override fun onDeleteRoomPressed(room: Room.Complete) {
        val accepted = room.participants.isEmpty() && room.questions.all { it.images.isEmpty() }
        val dialog = DialogState(props.context.getString(R.string.delete_class),
            onSubmitted = if (accepted) {{ vmRoom.onDeleteRoomPressed(room.id, ::onDeleteRoomSucceed) }} else null)
        vmMain.onDialogStateChange(dialog)
    }
}