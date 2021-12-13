package com.dudegenuine.whoknows.ui.view.participant

import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.Participant
import com.dudegenuine.usecase.participant.*
import com.dudegenuine.whoknows.ui.view.BaseViewModel
import com.dudegenuine.whoknows.ui.view.ViewState
import com.dudegenuine.whoknows.ui.view.ViewState.Companion.DONT_EMPTY
import com.dudegenuine.whoknows.ui.view.participant.contract.IParticipantViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
@HiltViewModel
class ParticipantViewModel

    @Inject constructor(
    private val postParticipantUseCase: PostParticipant,
    private val getParticipantUseCase: GetParticipant,
    private val patchParticipantUseCase: PatchParticipant,
    private val deleteParticipantUseCase: DeleteParticipant,
    private val getParticipantsUseCase: GetParticipants): BaseViewModel(), IParticipantViewModel {

    override fun postParticipant(participant: Participant) {
        if (participant.roomId.isEmpty() ||
            participant.userId.isEmpty() ||
            participant.currentPage.isEmpty() ||
            participant.timeLeft != 0){
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        participant.apply { createdAt = Date() }

        postParticipantUseCase(participant)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getParticipant(id: String) {
        if (id.isEmpty()){
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        getParticipantUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun patchParticipant(id: String, current: Participant) {
        if (id.isEmpty() || current.roomId.isEmpty() ||
            current.userId.isEmpty() ||
            current.currentPage.isEmpty() ||
            current.timeLeft != 0){
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        current.apply { updatedAt = Date() }

        patchParticipantUseCase(id, current)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun deleteParticipant(id: String) {
        if (id.isEmpty()){
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        deleteParticipantUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getParticipants(page: Int, size: Int) {
        if (size != 0){
            _state.value = ViewState(error = DONT_EMPTY)
            return
        }

        getParticipantsUseCase(page, size)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }
}