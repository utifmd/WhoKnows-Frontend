package com.dudegenuine.whoknows.infrastructure.di.viewmodel

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.SavedStateHandle
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.local.api.IPrefsFactory
import com.dudegenuine.local.api.IShareLauncher
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.*
import com.dudegenuine.whoknows.infrastructure.di.viewmodel.contract.IViewModelModule
import com.dudegenuine.whoknows.ui.vm.file.FileViewModel
import com.dudegenuine.whoknows.ui.vm.file.IFileViewModel
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule: IViewModelModule {

    @Provides
    @ViewModelScoped
    override fun provideMainActivityViewModel(
        prefsFactory: IPrefsFactory,
        messagingUseCaseModule: IMessageUseCaseModule,
        notifier: INotificationUseCaseModule,
        userUseCaseModule: IUserUseCaseModule,
        savedStateHandle: SavedStateHandle
    ): IActivityViewModel {

        return ActivityViewModel(prefsFactory, messagingUseCaseModule, notifier, userUseCaseModule, savedStateHandle)
    }

    @Provides
    @ViewModelScoped
    override fun provideUserViewModel(
        prefsFactory: IPrefsFactory,
        messaging: IMessageUseCaseModule,
        userUseCase: IUserUseCaseModule,
        fileCase: IFileUseCaseModule,
        savedStateHandle: SavedStateHandle
    ): IUserViewModel =

        UserViewModel(prefsFactory, messaging, userUseCase, fileCase, savedStateHandle)

    @Provides
    @ViewModelScoped
    override fun provideRoomViewModel(
        prefsFactory: IPrefsFactory,
        caseFile: IFileUseCaseModule,
        caseRoom: IRoomUseCaseModule,
        caseUser: IUserUseCaseModule,
        caseParticipant: IParticipantUseCaseModule,
        caseMessaging: IMessageUseCaseModule,
        caseQuiz: IQuizUseCaseModule,
        caseNotification: INotificationUseCaseModule,
        caseResult: IResultUseCaseModule,
        savedStateHandle: SavedStateHandle
    ): IRoomViewModel {
        return RoomViewModel(prefsFactory, caseMessaging, caseFile, caseNotification,
            caseRoom, caseUser, caseParticipant, caseQuiz, caseResult, savedStateHandle)
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
        prefsFactory: IPrefsFactory,
        case: INotificationUseCaseModule,
        caseUser: IUserUseCaseModule,
        savedStateHandle: SavedStateHandle
    ): INotificationViewModel {
        return NotificationViewModel(prefsFactory, case, caseUser, savedStateHandle)
    }


    @Provides
    @ViewModelScoped
    override fun provideFileViewModel(
        case: IFileUseCaseModule,
        launcher: IShareLauncher,
        savedStateHandle: SavedStateHandle): IFileViewModel =
        FileViewModel(case, launcher, savedStateHandle)
}