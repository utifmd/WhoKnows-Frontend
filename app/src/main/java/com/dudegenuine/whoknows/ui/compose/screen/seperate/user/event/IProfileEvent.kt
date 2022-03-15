package com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event


interface IProfileState

interface IProfileEvent {
    fun onFullNamePressed(it: String){}
    fun onPhonePressed(it: String){}
    fun onEmailPressed(it: String){}
    fun onUsernamePressed(it: String){}
    fun onPasswordPressed(it: String){}
    fun onBackPressed(){}
    fun onPicturePressed(fileId: String){}
    fun onSignOutPressed(){}

    companion object {
        const val NAME = "Full Name"
        const val USERNAME = "Username"
        const val PASSWORD = "Password"
        const val PHONE = "Phone Number"
        const val EMAIL = "Email Address"
        const val URL_PROFILE = "Image Url"
    }
}