package com.dudegenuine.whoknows.infrastructure.di.viewmodel.contract

import androidx.lifecycle.SavedStateHandle
import com.dudegenuine.usecase.user.*
import com.dudegenuine.whoknows.ui.view.UserViewModel

/**
 * Fri, 03 Dec 2021
 * WhoKnows by utifmd
 **/
interface IViewModelModule {
    fun provideUserViewModel(
        postUser: PostUser,
        getUser: GetUser,
        patchUser: PatchUser,
        deleteUser: DeleteUser,
        getUsers: GetUsers,
        signInUser: SignInUser
    ): UserViewModel
}