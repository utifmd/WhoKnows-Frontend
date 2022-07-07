package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.contract.*
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory
import com.dudegenuine.usecase.participant.*
import com.dudegenuine.usecase.participation.DeleteParticipation
import com.dudegenuine.usecase.participation.PostParticipation
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IParticipantUseCaseModule

/**
 * Sat, 08 Jan 2022
 * WhoKnows by utifmd
 **/
class ParticipantUseCaseModule(
    private val reposParticipant: IParticipantRepository,
    private val reposRoom: IRoomRepository,
    private val reposResult: IResultRepository,
    private val reposNotification: INotificationRepository,
    private val reposMessaging: IMessagingRepository): IParticipantUseCaseModule {

    override val postParticipant: PostParticipant
        get() = PostParticipant(reposParticipant)

    override val getParticipant: GetParticipant
        get() = GetParticipant(reposParticipant)

    override val patchParticipant: PatchParticipant
        get() = PatchParticipant(reposParticipant)

    override val deleteParticipant: DeleteParticipant
        get() = DeleteParticipant(reposParticipant)

    override val getParticipants: GetParticipants
        get() = GetParticipants(reposParticipant)

    override val getParticipation: GetParticipation
        get() = GetParticipation(reposParticipant)

    override val postParticipation: PostParticipation
        get() = PostParticipation(
            reposParticipant = reposParticipant,
            reposRoom = reposRoom,
            reposResult = reposResult,
            reposMessaging = reposMessaging,
            reposNotify = reposNotification
        )

    override val deleteParticipation: DeleteParticipation
        get() = DeleteParticipation(
            reposParticipant = reposParticipant,
            reposResult = reposResult,
            reposMessaging = reposMessaging,
            reposNotify = reposNotification
        )

    override val prefs: IPrefsFactory
        get() = reposParticipant.prefs
}