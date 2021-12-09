package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.QuizRepository
import com.dudegenuine.usecase.quiz.*
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IQuizUseCaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
@Module
@InstallIn(ViewModelComponent::class)
object QuizUseCaseModule: IQuizUseCaseModule {

    @Provides
    @ViewModelScoped
    override fun providePostQuizModule(repos: QuizRepository): PostQuiz {
        return PostQuiz(repos)
    }

    @Provides
    @ViewModelScoped
    override fun provideGetQuizModule(repos: QuizRepository): GetQuiz {
        return GetQuiz(repos)
    }

    @Provides
    @ViewModelScoped
    override fun providePatchQuizModule(repos: QuizRepository): PatchQuiz {
        return PatchQuiz(repos)
    }

    @Provides
    @ViewModelScoped
    override fun provideDeleteQuizModule(repos: QuizRepository): DeleteQuiz {
        return DeleteQuiz(repos)
    }

    @Provides
    @ViewModelScoped
    override fun provideGetQuestionsModule(repos: QuizRepository): GetQuestions {
        return GetQuestions(repos)
    }
}