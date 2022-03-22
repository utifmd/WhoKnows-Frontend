package com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event

import androidx.navigation.NavHostController
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.IProfileEvent.Companion.EMAIL
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.IProfileEvent.Companion.NAME
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.IProfileEvent.Companion.PASSWORD
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.IProfileEvent.Companion.PHONE
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.IProfileEvent.Companion.USERNAME

/**
 * Wed, 26 Jan 2022
 * WhoKnows by utifmd
 **/
class ProfileEvent(
    val router: NavHostController,
    val onSignOutClicked: () -> Unit): IProfileEvent {

    override fun onPicturePressed(fileId: String?) {
        if(fileId.isNullOrBlank()) return

        router.navigate(Screen.Home.Preview.routeWithArgs(fileId))
    }

    override fun onFullNamePressed(it: String){
        router.navigate(Screen.Home.Setting.ProfileEditor.routeWithArgs(NAME, it))}

    override fun onPhonePressed(it: String){
        router.navigate(Screen.Home.Setting.ProfileEditor.routeWithArgs(PHONE, it))}

    override fun onEmailPressed(it: String){
        router.navigate(Screen.Home.Setting.ProfileEditor.routeWithArgs(EMAIL, it))}

    override fun onUsernamePressed(it: String){
        router.navigate(Screen.Home.Setting.ProfileEditor.routeWithArgs(USERNAME, it))}

    override fun onPasswordPressed(it: String){
        router.navigate(Screen.Home.Setting.ProfileEditor.routeWithArgs(PASSWORD, it))}

    override fun onSignOutPressed() { onSignOutClicked() }
}