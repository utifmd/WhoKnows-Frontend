package com.dudegenuine.whoknows.ux.vm.user.contract

import androidx.paging.PagingData
import com.dudegenuine.model.User
import com.dudegenuine.whoknows.ux.vm.BaseViewModel
import kotlinx.coroutines.flow.Flow

/**
 * Sat, 04 Dec 2021
 * WhoKnows by utifmd
 **/
abstract class IUserViewModel: /*IMessagingViewModel, */IProfileEvent, BaseViewModel() { //: IFilePresenter {
    //fun signInUser(loginRequest: LoginRequest)
    open fun registerUser(){}
    open fun loginUser(){}
    open fun logoutUser(){}
    open fun postUser(user: User.Complete){}
    open fun getUser(){}
    open fun getUser(id: String){}
    open fun getUser(id: String, onSucceed: (User.Complete) -> Unit){}
    open fun patchUser(id: String, freshUser: User.Complete){}
    open fun patchUser(freshUser: User.Complete, onSucceed: (User.Complete) -> Unit){}
    open fun deleteUser(id: String){}
    open fun getUsers(page: Int, size: Int){}

    abstract val participants: Flow<PagingData<User.Censored>>

    companion object {
        const val USER_ID_SAVED_KEY = "is_own_saved_user"
        const val DEFAULT_BATCH_PARTICIPANT = 5
        //const val OWN_USER_TRUE = "true"
    }
}