package com.dudegenuine.whoknows.ui.compose.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    items: List<BottomDomain>,
    controller: NavController) {
    val backStackEntry = controller.currentBackStackEntryAsState()

    BottomNavigation(
        modifier = modifier,
        backgroundColor = Color.White,
        elevation = 3.dp) {

        items.forEach { screen ->
            val currentDestination = backStackEntry.value?.destination
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true /*screen.route == backStackEntry.value?.destination?.route*/

            BottomNavigationItem(
                selected = isSelected,
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = MaterialTheme.colors.onBackground,
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
                },
                icon = {
                    Column(horizontalAlignment = CenterHorizontally) {
                        if (screen.badge > 0) {
                            BadgedBox(
                                badge = {
                                    Text(text = screen.badge.toString())
                                }
                            ) {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.name
                                )
                            }
                        } else {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.name
                            )
                        }

                        Text(
                            text = screen.name,
                            textAlign = TextAlign.Center,
                            fontSize = 10.sp
                        ) // if (isSelected)
                    }
                }
            )
        }
    }
}