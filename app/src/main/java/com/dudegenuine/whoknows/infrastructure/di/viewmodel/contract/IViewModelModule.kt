package com.dudegenuine.whoknows.infrastructure.di.viewmodel.contract

import androidx.lifecycle.SavedStateHandle
import com.dudegenuine.usecase.user.GetUser
import com.dudegenuine.usecase.user.GetUsers
import com.dudegenuine.whoknows.ui.view.UserViewModel

/**
 * Fri, 03 Dec 2021
 * WhoKnows by utifmd
 **/
interface IViewModelModule {
    fun provideUserViewModel(getUser: GetUser, getUsers: GetUsers): UserViewModel
}