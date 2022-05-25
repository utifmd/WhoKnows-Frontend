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
import com.dudegenuine.whoknows.ui.vm.main.ActivityViewModel

/**
 * Wed, 26 Jan 2022
 * WhoKnows by utifmd
 **/
class ProfileEvent(
    private val props: IMainProps): IProfileEvent {
    private val vmMain = props.vmMain as ActivityViewModel

    override fun onShowSnackBar(message: String) {
        vmMain.onShowSnackBar(message)
    }

    override fun onBackPressed() {
        props.router.popBackStack()
        /*props.router.navigate(Screen.Home.route){
            popUpTo(Screen.Home.route) { inclusive = true }
        }*/
    }

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

    override fun onSignOutPressed(onSubmitted: () -> Unit) {
        val dialog = DialogState(props.context.getString(R.string.logout_account), onSubmitted = onSubmitted)

        props.vmMain.onShowDialog(dialog)
    }
}