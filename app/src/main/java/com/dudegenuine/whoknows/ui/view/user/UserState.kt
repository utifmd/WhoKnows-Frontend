package com.dudegenuine.whoknows.ui.view.user

import com.dudegenuine.model.User

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/
data class UserState(
    val loading: Boolean = false,
    val data: User? = null,
    val error: String = ""
)
