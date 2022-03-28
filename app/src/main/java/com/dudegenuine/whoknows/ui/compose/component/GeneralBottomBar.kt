package com.dudegenuine.whoknows.ui.compose.component

import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dudegenuine.whoknows.ui.compose.model.BottomDomain

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun GeneralBottomBar(
    modifier: Modifier = Modifier,
    items: Set<BottomDomain>,
    darkTheme: Boolean = isSystemInDarkTheme(),
    controller: NavController, onPressed: ((BottomDomain) -> Unit)? = null) {
    val backStackEntry = controller.currentBackStackEntryAsState()

    BottomNavigation(modifier,
        backgroundColor = if (darkTheme) MaterialTheme.colors.background
            else MaterialTheme.colors.onPrimary,
        contentColor = if (darkTheme) MaterialTheme.colors.onBackground
            else MaterialTheme.colors.primary,
        elevation = 3.dp) {

        items.forEach { screen ->
            val currentDestination = backStackEntry.value?.destination
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true /* screen.route == backStackEntry.value?.destination?.route*/

            BottomNavigationItem(
                selected = isSelected,
                onClick = {
                    controller.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(controller.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same screen
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected screen
                        restoreState = true
                    }

                    onPressed?.invoke(screen)
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