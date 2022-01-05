package com.dudegenuine.whoknows.ui.presenter.user.contract

import com.dudegenuine.model.User
import com.dudegenuine.model.request.LoginRequest
import com.dudegenuine.whoknows.ui.presenter.file.IFilePresenter

/**
 * Sat, 04 Dec 2021
 * WhoKnows by utifmd
 **/
interface IUserViewModel: IFilePresenter {
    fun signInUser(loginRequest: LoginRequest)
    fun postUser(user: User)
    fun getUser(id: String)
    fun patchUser(id: String, current: User)
    fun deleteUser(id: String)
    fun getUsers(page: Int, size: Int)
}