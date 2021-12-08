package com.dudegenuine.whoknows.infrastructure.di.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.dudegenuine.usecase.user.*
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
        postUser: PostUser,
        getUser: GetUser,
        patchUser: PatchUser,
        deleteUser: DeleteUser,
        getUsers: GetUsers,
        signInUser: SignInUser
        //savedStateHandle: SavedStateHandle
    ): UserViewModel {

        return UserViewModel(postUser, getUser, patchUser, deleteUser, getUsers, signInUser)
    }
}