package com.dudegenuine.whoknows.ux.vm.user.contract

interface IProfileEvent {
    fun onShowSnackBar(message: String)

    fun onFullNamePressed(it: String)
    fun onPhonePressed(it: String)
    fun onEmailPressed(it: String)
    fun onUsernamePressed(it: String)
    fun onPasswordPressed(it: String)
    fun onBackPressed()
    fun onPicturePressed(fileId: String?)
    fun onSignOutPressed()

    companion object {
        const val KEY_EDIT_FIELD_TYPE = "KEY_EDIT_FIELD_TYPE"
        const val KEY_EDIT_FIELD_VALUE = "KEY_EDIT_FIELD_VALUE"

        const val NAME = "Full Name"
        const val USERNAME = "Username"
        const val PASSWORD = "Password"
        const val PHONE = "Phone Number"
        const val EMAIL = "Email Address"
        const val URL_PROFILE = "Image Url"
    }
}