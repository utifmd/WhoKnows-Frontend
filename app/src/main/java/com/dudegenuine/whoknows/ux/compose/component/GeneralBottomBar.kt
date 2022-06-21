package com.dudegenuine.whoknows.ux.compose.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dudegenuine.whoknows.ux.compose.model.BottomDomain
import com.dudegenuine.whoknows.ux.compose.navigation.Screen
import com.dudegenuine.whoknows.ux.vm.file.IFileViewModel.Companion.PREVIEW_FILE_ID
import com.dudegenuine.whoknows.ux.vm.room.contract.IRoomEvent.Companion.ROOM_ID_SAVED_KEY
import com.dudegenuine.whoknows.ux.vm.user.contract.IUserViewModel.Companion.USER_ID_SAVED_KEY

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun GeneralBottomBar(
    modifier: Modifier = Modifier,
    items: Set<BottomDomain>, //darkTheme: Boolean = isSystemInDarkTheme(),
    controller: NavController, onPressed: ((BottomDomain) -> Unit)? = null) {
    val backStackEntry = controller.currentBackStackEntryAsState()

    /*AnimatedVisibility(visible = ) {

    }*/

    BottomNavigation(modifier,
        backgroundColor = /*if (darkTheme)*/ MaterialTheme.colors.background /*else MaterialTheme.colors.onPrimary*/,
        contentColor = /*if (darkTheme)*/ MaterialTheme.colors.onBackground /*else MaterialTheme.colors.primary*/,
        elevation = 3.dp) {

        items.forEach { screen ->
            val currentDestination = backStackEntry.value?.destination

            val hasLinkUser = Screen.Home.Summary.RoomDetail.ProfileDetail.uriWithArgs("{$USER_ID_SAVED_KEY}").let { currentDestination?.hasDeepLink(it.toUri()) }
            val hasLinkRoom = Screen.Home.Summary.RoomDetail.uriWithArgs("{$ROOM_ID_SAVED_KEY}").let { currentDestination?.hasDeepLink(it.toUri()) } //currentDestination?.hasDeepLink(Uri.parse("${BuildConfig.BASE_CLIENT_URL}/who-knows/room/{$ROOM_ID_SAVED_KEY}}"))
            val hasLinkNotifier = Screen.Home.Summary.Notification.uriPattern?.let { currentDestination?.hasDeepLink(it.toUri()) }
            val hasLinkView = Screen.Home.Preview.uriWithArgs("{$PREVIEW_FILE_ID}").let { currentDestination?.hasDeepLink(it.toUri()) }
            val hasDeeplink = hasLinkUser == true || hasLinkRoom == true || hasLinkNotifier == true || hasLinkView == true
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true /* screen.route == currentDestination?.route*/  // currentDestination?.route == Screen.Home.Summary.route

            BottomNavigationItem(
                selected = isSelected,
                onClick = {
                    /*Log.d("GeneralBottomBar: ", "route ${currentDestination?.route}")
                    Log.d("GeneralBottomBar: ", "hasDeeplink $hasDeeplink")*/

                    if (hasDeeplink){
                        controller.popBackStack()
                        controller.navigate(screen.route)
                    } else {
                        controller.navigate(screen.route) {
                            popUpTo(controller.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }

                        onPressed?.invoke(screen)
                    }
                },
                icon = {
                    Column(horizontalAlignment = CenterHorizontally) {
                        if (screen.badge > 0) GeneralBadgeBox(screen)
                        else Icon(screen.icon, contentDescription = null)

                        Text(screen.name,
                            textAlign = TextAlign.Center,
                            fontSize = 10.sp
                        )
                    }
                }
            )
        }
    }
}
@Composable
private fun GeneralBadgeBox(screen: BottomDomain,
    modifier: Modifier = Modifier){

    BadgedBox(
        modifier = modifier.padding(top = 8.dp),
        badge = {
            Surface(
                color = MaterialTheme.colors.error,
                shape = CircleShape) {

                Text(screen.badge.toString(),
                    modifier = modifier.padding(2.dp),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onError)
            }
        },
        content = {
            Icon(screen.icon, contentDescription = screen.name)
        }
    )
}