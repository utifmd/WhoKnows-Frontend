package com.dudegenuine.whoknows.infrastructure.di.viewmodel

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.lifecycle.SavedStateHandle
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.*
import com.dudegenuine.whoknows.infrastructure.di.viewmodel.contract.IViewModelModule
import com.dudegenuine.whoknows.ui.vm.main.ActivityViewModel
import com.dudegenuine.whoknows.ui.vm.main.IActivityViewModel
import com.dudegenuine.whoknows.ui.vm.notification.NotificationViewModel
import com.dudegenuine.whoknows.ui.vm.notification.contract.INotificationViewModel
import com.dudegenuine.whoknows.ui.vm.participant.ParticipantViewModel
import com.dudegenuine.whoknows.ui.vm.participant.contract.IParticipantViewModel
import com.dudegenuine.whoknows.ui.vm.quiz.QuizViewModel
import com.dudegenuine.whoknows.ui.vm.quiz.contract.IQuizViewModel
import com.dudegenuine.whoknows.ui.vm.result.ResultViewModel
import com.dudegenuine.whoknows.ui.vm.result.contract.IResultViewModel
import com.dudegenuine.whoknows.ui.vm.room.RoomViewModel
import com.dudegenuine.whoknows.ui.vm.room.contract.IRoomViewModel
import com.dudegenuine.whoknows.ui.vm.user.UserViewModel
import com.dudegenuine.whoknows.ui.vm.user.contract.IUserViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule: IViewModelModule {

    @Provides
    @ViewModelScoped
    override fun provideMainActivityViewModel(
        messagingUseCaseModule: IMessageUseCaseModule,
        userUseCaseModule: IUserUseCaseModule,
        savedStateHandle: SavedStateHandle): IActivityViewModel {

        return ActivityViewModel(messagingUseCaseModule, userUseCaseModule, savedStateHandle)
    }

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
        caseRoom: IRoomUseCaseModule,
        caseUser: IUserUseCaseModule,
        caseParticipant: IParticipantUseCaseModule,
        caseMessagig: IMessageUseCaseModule,
        caseFile: IFileUseCaseModule,
        caseResult: IResultUseCaseModule,
        savedStateHandle: SavedStateHandle): IRoomViewModel {
        return RoomViewModel(caseMessagig, caseFile, caseRoom, caseUser, caseParticipant, caseResult, savedStateHandle)
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