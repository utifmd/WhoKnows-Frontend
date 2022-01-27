package com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event

import androidx.navigation.NavHostController
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.IProfileEvent.Companion.NAME
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.IProfileEvent.Companion.PHONE
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.IProfileEvent.Companion.EMAIL
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.IProfileEvent.Companion.USERNAME
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.IProfileEvent.Companion.PASSWORD

/**
 * Wed, 26 Jan 2022
 * WhoKnows by utifmd
 **/
class ProfileEvent(
    val router: NavHostController, onSignOutPressed: () -> Unit): IProfileEvent {

    override fun onFullNamePressed(it: String){
        router.navigate(Screen.Home.Setting.ProfileEditor.withArgs(NAME, it))}

    override fun onPhonePressed(it: String){
        router.navigate(Screen.Home.Setting.ProfileEditor.withArgs(PHONE, it))}

    override fun onEmailPressed(it: String){
        router.navigate(Screen.Home.Setting.ProfileEditor.withArgs(EMAIL, it))}

    override fun onUsernamePressed(it: String){
        router.navigate(Screen.Home.Setting.ProfileEditor.withArgs(USERNAME, it))}

    override fun onPasswordPressed(it: String){
        router.navigate(Screen.Home.Setting.ProfileEditor.withArgs(PASSWORD, it))}

    override val onSignOutPressed: () -> Unit = { onSignOutPressed() }
}