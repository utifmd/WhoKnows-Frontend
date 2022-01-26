package com.dudegenuine.whoknows.ui.presenter.user.contract

import com.dudegenuine.model.User

/**
 * Sat, 04 Dec 2021
 * WhoKnows by utifmd
 **/
interface IUserViewModel { //: IFilePresenter {
    //fun signInUser(loginRequest: LoginRequest)
    fun signInUser()
    fun signUpUser()
    fun signOutUser()
    fun postUser(user: User)
    fun getUser()
    fun getUser(id: String)
    fun patchUser(id: String, freshUser: User)
    fun patchUser(freshUser: User, onSucceed: (User) -> Unit)
    fun deleteUser(id: String)
    fun getUsers(page: Int, size: Int)
}