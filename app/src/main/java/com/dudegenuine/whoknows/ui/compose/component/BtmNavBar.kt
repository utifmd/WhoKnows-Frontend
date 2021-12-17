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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dudegenuine.whoknows.ui.compose.model.BtmNavItem

/**
 * Thu, 16 Dec 2021
 * WhoKnows by utifmd
 **/
@Composable
fun BtmNavBar(items: List<BtmNavItem>, controller: NavController, modifier: Modifier = Modifier, onItemClick: (BtmNavItem) -> Unit) {
    val backStackEntry = controller.currentBackStackEntryAsState()

    BottomNavigation(
        modifier = modifier,
        backgroundColor = Color.White,
        elevation = 3.dp) {

        items.forEach { item ->
            val isSelected = item.route == backStackEntry.value?.destination?.route

            BottomNavigationItem(
                selected = isSelected,
                selectedContentColor = Color.Gray,
                unselectedContentColor = Color.DarkGray,
                onClick = { onItemClick(item) },
                icon = {
                    Column(horizontalAlignment = CenterHorizontally) {
                        if (item.badge > 0) {
                            BadgedBox(
                                badge = {
                                    Text(text = item.badge.toString())
                                }
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.name
                                )
                            }
                        } else {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.name
                            )
                        }

                        Text(
                            text = item.name,
                            textAlign = TextAlign.Center,
                            fontSize = 10.sp
                        ) // if (isSelected)
                    }
                }
            )
        }
    }
}