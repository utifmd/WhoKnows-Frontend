package com.dudegenuine.whoknows.ui.vm.user.contract

import androidx.paging.PagingData
import com.dudegenuine.model.User
import com.dudegenuine.whoknows.ui.vm.notification.contract.IMessagingViewModel
import kotlinx.coroutines.flow.Flow

/**
 * Sat, 04 Dec 2021
 * WhoKnows by utifmd
 **/
interface IUserViewModel: IMessagingViewModel { //: IFilePresenter {
    //fun signInUser(loginRequest: LoginRequest)
    fun signInUser()
    fun signUpUser()
    fun signOutUser()
    fun postUser(user: User.Complete)
    fun getUser(){}
    fun getUser(id: String)
    fun getUser(id: String, onSucceed: (User.Complete) -> Unit)
    fun patchUser(id: String, freshUser: User.Complete)
    fun patchUser(freshUser: User.Complete, onSucceed: (User.Complete) -> Unit)
    fun deleteUser(id: String)
    fun getUsers(page: Int, size: Int)

    val participants: Flow<PagingData<User.Censored>>

    companion object {
        const val USER_ID_SAVED_KEY = "is_own_saved_user"
        //const val OWN_USER_TRUE = "true"
    }
}