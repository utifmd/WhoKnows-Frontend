package com.dudegenuine.whoknows.infrastructure.di.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.dudegenuine.usecase.user.GetUser
import com.dudegenuine.usecase.user.GetUsers
import com.dudegenuine.whoknows.infrastructure.di.viewmodel.contract.IViewModelModule
import com.dudegenuine.whoknows.ui.view.UserViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule: IViewModelModule {

    @Provides
    @Singleton
    override fun provideUserViewModel(
        getUser: GetUser,
        getUsers: GetUsers
        //savedStateHandle: SavedStateHandle
    ): UserViewModel {

        return UserViewModel(getUser, getUsers)
    }
}