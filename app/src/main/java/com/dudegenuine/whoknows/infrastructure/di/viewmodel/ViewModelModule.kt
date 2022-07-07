package com.dudegenuine.whoknows.infrastructure.di.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory
import com.dudegenuine.repository.contract.dependency.local.IShareLauncher
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.*
import com.dudegenuine.whoknows.infrastructure.di.viewmodel.contract.IViewModelModule
import com.dudegenuine.whoknows.ux.vm.file.FileViewModel
import com.dudegenuine.whoknows.ux.vm.file.IFileViewModel
import com.dudegenuine.whoknows.ux.vm.main.IMainViewModel
import com.dudegenuine.whoknows.ux.vm.main.MainViewModel
import com.dudegenuine.whoknows.ux.vm.notification.NotificationViewModel
import com.dudegenuine.whoknows.ux.vm.notification.contract.INotificationViewModel
import com.dudegenuine.whoknows.ux.vm.participation.ParticipationViewModel
import com.dudegenuine.whoknows.ux.vm.participation.contract.IParticipantViewModel
import com.dudegenuine.whoknows.ux.vm.quiz.QuizViewModel
import com.dudegenuine.whoknows.ux.vm.quiz.contract.IQuizViewModel
import com.dudegenuine.whoknows.ux.vm.result.ResultViewModel
import com.dudegenuine.whoknows.ux.vm.result.contract.IResultViewModel
import com.dudegenuine.whoknows.ux.vm.room.RoomViewModel
import com.dudegenuine.whoknows.ux.vm.room.contract.IRoomViewModel
import com.dudegenuine.whoknows.ux.vm.user.UserViewModel
import com.dudegenuine.whoknows.ux.vm.user.contract.IUserViewModel
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
    override fun provideMainActivityViewModel(
        messagingUseCaseModule: IMessageUseCaseModule,
        notifier: INotificationUseCaseModule,
        userUseCaseModule: IUserUseCaseModule,
        roomUseCaseModule: IRoomUseCaseModule,
        quizUseCaseModule: IQuizUseCaseModule,
        caseImpression: IImpressionUseCaseModule,
        savedStateHandle: SavedStateHandle
    ): IMainViewModel {

        return MainViewModel(messagingUseCaseModule, notifier, userUseCaseModule, roomUseCaseModule, caseImpression, quizUseCaseModule, savedStateHandle)
    }

    @Provides
    @ViewModelScoped
    override fun provideUserViewModel(
        prefsFactory: IPrefsFactory,
        userUseCase: IUserUseCaseModule,
        fileCase: IFileUseCaseModule,
        savedStateHandle: SavedStateHandle
    ): IUserViewModel =

        UserViewModel(userUseCase, fileCase, savedStateHandle)

    @Provides
    @ViewModelScoped
    override fun provideRoomViewModel(
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
        return RoomViewModel(caseMessaging, caseFile, caseNotification,
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
        roomUseCaseModule: IRoomUseCaseModule,
        userUseCaseModule: IUserUseCaseModule,
        savedStateHandle: SavedStateHandle): IParticipantViewModel {

        return ParticipationViewModel(participantUseCaseModule, roomUseCaseModule, userUseCaseModule, savedStateHandle)
    }

    @Provides
    @ViewModelScoped
    override fun provideNotificationViewModel(
        prefsFactory: IPrefsFactory,
        case: INotificationUseCaseModule,
        savedStateHandle: SavedStateHandle
    ): INotificationViewModel {
        return NotificationViewModel(prefsFactory, case, savedStateHandle)
    }


    @Provides
    @ViewModelScoped
    override fun provideFileViewModel(
        case: IFileUseCaseModule,
        launcher: IShareLauncher,
        savedStateHandle: SavedStateHandle): IFileViewModel =
        FileViewModel(case, launcher, savedStateHandle)
}