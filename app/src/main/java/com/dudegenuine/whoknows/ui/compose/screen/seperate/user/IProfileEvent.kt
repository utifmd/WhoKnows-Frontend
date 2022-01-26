package com.dudegenuine.whoknows.ui.compose.screen.seperate.user

interface IProfileEvent {
    val onFullNamePressed: (String) -> Unit
    val onPhonePressed: (String) -> Unit
    val onEmailPressed: (String) -> Unit
    val onUsernamePressed: (String) -> Unit
    val onPasswordPressed: (String) -> Unit
    val onSignOutPressed: () -> Unit

    companion object {
        const val NAME = "Full Name"
        const val USERNAME = "Username"
        const val PASSWORD = "Password"
        const val PHONE = "Phone Number"
        const val EMAIL = "Email Address"
    }
}