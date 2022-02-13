package com.dudegenuine.whoknows.infrastructure.di.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.*
import com.dudegenuine.whoknows.infrastructure.di.viewmodel.contract.IViewModelModule
import com.dudegenuine.whoknows.ui.presenter.notification.NotificationViewModel
import com.dudegenuine.whoknows.ui.presenter.notification.contract.INotificationViewModel
import com.dudegenuine.whoknows.ui.presenter.participant.ParticipantViewModel
import com.dudegenuine.whoknows.ui.presenter.participant.contract.IParticipantViewModel
import com.dudegenuine.whoknows.ui.presenter.quiz.QuizViewModel
import com.dudegenuine.whoknows.ui.presenter.quiz.contract.IQuizViewModel
import com.dudegenuine.whoknows.ui.presenter.result.ResultViewModel
import com.dudegenuine.whoknows.ui.presenter.result.contract.IResultViewModel
import com.dudegenuine.whoknows.ui.presenter.room.RoomViewModel
import com.dudegenuine.whoknows.ui.presenter.room.contract.IRoomViewModel
import com.dudegenuine.whoknows.ui.presenter.user.UserViewModel
import com.dudegenuine.whoknows.ui.presenter.user.contract.IUserViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule: IViewModelModule {

    @Provides
    @ViewModelScoped
    override fun provideUserViewModel(
        userUseCase: IUserUseCaseModule,
        fileCase: IFileUseCaseModule,
        savedStateHandle: SavedStateHandle): IUserViewModel =

        UserViewModel(userUseCase, fileCase, savedStateHandle)

    @Provides
    @ViewModelScoped
    override fun provideRoomViewModel(
        roomUseCaseModule: IRoomUseCaseModule,
        participantUseCaseModule: IParticipantUseCaseModule,
        resultUseCaseModule: IResultUseCaseModule,
        fileCase: IFileUseCaseModule,
        savedStateHandle: SavedStateHandle
    ): IRoomViewModel {

        return RoomViewModel(roomUseCaseModule, participantUseCaseModule, resultUseCaseModule, fileCase, savedStateHandle)
    }

    @Provides
    @ViewModelScoped
    override fun provideQuizViewModel(
        quizUseCaseModule: IQuizUseCaseModule,
        fileUseCaseModule: IFileUseCaseModule,
        savedStateHandle: SavedStateHandle): IQuizViewModel {

        return QuizViewModel(quizUseCaseModule, fileUseCaseModule, savedStateHandle)
    }

    @Provides
    @ViewModelScoped
    override fun provideResultViewModel(
        userUseCaseModule: IUserUseCaseModule,
        resultUseCaseModule: IResultUseCaseModule,
        savedStateHandle: SavedStateHandle

    ): IResultViewModel = ResultViewModel(userUseCaseModule, resultUseCaseModule, savedStateHandle)

    @Provides
    @ViewModelScoped
    override fun provideParticipantViewModel(
        participantUseCaseModule: IParticipantUseCaseModule,
        savedStateHandle: SavedStateHandle
    ): IParticipantViewModel {

        return ParticipantViewModel(participantUseCaseModule, savedStateHandle)
    }

    @Provides
    @ViewModelScoped
    override fun provideNotificationViewModel(
        case: INotificationUseCaseModule,
        savedStateHandle: SavedStateHandle
    ): INotificationViewModel {
        return NotificationViewModel(case, savedStateHandle)
    }
}