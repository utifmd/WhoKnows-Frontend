package com.dudegenuine.whoknows.infrastructure.di.viewmodel

import com.dudegenuine.usecase.room.GetRoom
import com.dudegenuine.usecase.user.*
import com.dudegenuine.whoknows.infrastructure.di.viewmodel.contract.IViewModelModule
import com.dudegenuine.whoknows.ui.view.room.RoomViewModel
import com.dudegenuine.whoknows.ui.view.user.UserViewModel
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
    @Provides
    @ViewModelScoped
    override fun provideRoomViewModel(
//        postRoom: PostRoom,
        getRoom: GetRoom,
//        patchRoom: PatchRoom,
//        deleteRoom: DeleteRoom,
//        getRooms: GetRooms,
//        signInRoom: SignInRoom,
    ): RoomViewModel {
        return RoomViewModel(
//            postRoom,
            getRoom,
//            patchRoom,
//            deleteRoom,
//            getRooms,
//            signInRoom,
        )
    }
}