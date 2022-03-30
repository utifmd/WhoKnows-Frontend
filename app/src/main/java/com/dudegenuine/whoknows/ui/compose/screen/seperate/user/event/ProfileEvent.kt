package com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event

import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ui.compose.navigation.Screen
import com.dudegenuine.whoknows.ui.compose.screen.seperate.main.IMainProps
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.IProfileEvent.Companion.EMAIL
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.IProfileEvent.Companion.NAME
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.IProfileEvent.Companion.PASSWORD
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.IProfileEvent.Companion.PHONE
import com.dudegenuine.whoknows.ui.compose.screen.seperate.user.event.IProfileEvent.Companion.USERNAME
import com.dudegenuine.whoknows.ui.compose.state.DialogState
import com.dudegenuine.whoknows.ui.vm.user.UserViewModel
import kotlinx.coroutines.FlowPreview

/**
 * Wed, 26 Jan 2022
 * WhoKnows by utifmd
 **/
@FlowPreview
class ProfileEvent(
    private val props: IMainProps): IProfileEvent {
    val vmUser = props.vmUser as UserViewModel

    override fun onPicturePressed(fileId: String?) {
        if(fileId.isNullOrBlank()) return

        props.router.navigate(Screen.Home.Preview.routeWithArgs(fileId))
    }

    override fun onFullNamePressed(it: String){
        props.router.navigate(Screen.Home.Setting.ProfileEditor.routeWithArgs(NAME, it))}

    override fun onPhonePressed(it: String){
        props.router.navigate(Screen.Home.Setting.ProfileEditor.routeWithArgs(PHONE, it))}

    override fun onEmailPressed(it: String){
        props.router.navigate(Screen.Home.Setting.ProfileEditor.routeWithArgs(EMAIL, it))}

    override fun onUsernamePressed(it: String){
        props.router.navigate(Screen.Home.Setting.ProfileEditor.routeWithArgs(USERNAME, it))}

    override fun onPasswordPressed(it: String){
        props.router.navigate(Screen.Home.Setting.ProfileEditor.routeWithArgs(PASSWORD, it))}

    override fun onSignOutPressed() {
        props.vmMain.onDialogStateChange(
            DialogState(props.context.getString(R.string.logout_account),
                true, null, vmUser::signOutUser)
        )
    }
}