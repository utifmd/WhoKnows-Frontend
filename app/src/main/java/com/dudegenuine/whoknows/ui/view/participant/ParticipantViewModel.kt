package com.dudegenuine.whoknows.ui.view.participant

import androidx.lifecycle.viewModelScope
import com.dudegenuine.model.Participant
import com.dudegenuine.usecase.participant.*
import com.dudegenuine.whoknows.ui.view.BaseViewModel
import com.dudegenuine.whoknows.ui.view.participant.contract.IParticipantViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    private val getParticipantsUseCase: GetParticipants
): BaseViewModel(), IParticipantViewModel {

    override fun postParticipant(participant: Participant) {
        postParticipantUseCase(participant)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getParticipant(id: String) {
        getParticipantUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun patchParticipant(id: String, current: Participant) {
        patchParticipantUseCase(id, current)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun deleteParticipant(id: String) {
        deleteParticipantUseCase(id)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }

    override fun getParticipants(page: Int, size: Int) {
        getParticipantsUseCase(page, size)
            .onEach(this::resourcing).launchIn(viewModelScope)
    }
}