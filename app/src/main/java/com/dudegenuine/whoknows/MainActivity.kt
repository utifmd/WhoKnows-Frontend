package com.dudegenuine.whoknows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Explore
import com.dudegenuine.whoknows.ui.compose.model.BtmNavItem
import com.dudegenuine.whoknows.ui.compose.model.BtmNavItem.Companion.DISCOVER
import com.dudegenuine.whoknows.ui.compose.model.BtmNavItem.Companion.SETTING
import com.dudegenuine.whoknows.ui.compose.model.BtmNavItem.Companion.SUMMARY
import com.dudegenuine.whoknows.ui.compose.screen.RoomScreen
import com.dudegenuine.whoknows.ui.theme.WhoKnowsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalFoundationApi
@ExperimentalMaterialApi
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhoKnowsTheme {
                RoomScreen()

                /*val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        BtmNavBar(
                            items = bottomBarItems(),
                            controller = navController,
                            onItemClick = { navController.navigate(it.route) })
                    }
                    -pa
                ) {
                    Navigation(
                        controller = navController
                    )
                }*/
            }
        }
    }

    private fun bottomBarItems(): List<BtmNavItem> {
        return listOf(
            BtmNavItem(
                name = SUMMARY,
                route = SUMMARY.lowercase(),
                icon = Icons.Default.Block
            ),

            BtmNavItem(
                name = DISCOVER,
                route = DISCOVER.lowercase(),
                icon = Icons.Default.Explore //, badge = 2
            ),

            BtmNavItem(
                name = SETTING,
                route = SETTING.lowercase(),
                icon = Icons.Default.Edit
            )
        )
    }
}

//@Composable
//fun LoginScreen(
//    viewModel: RoomViewModel = hiltViewModel()){
//    // val state = viewModel.state.value
//
//    Column {
//        GeneralTextField(
//            viewState = viewModel.state,
//            onValueChange = viewModel::fillInput
//        )
//        Button(
//            onClick = viewModel::onContinueClick,
//            enabled = viewModel.state.value.participant?.isPropsNotBlank ?: false) {
//            Text(text = "Continue")
//        }
//    }
//}
//
//@Composable
//fun GeneralTextField(
//    viewState: State<ViewState>,
//    onValueChange: (text: String) -> Unit
//){
//    val field = viewState.value
//    Column {
//        TextField(
//            value = if(field.participant != null) field.participant.id else "",
//            onValueChange = { onValueChange(it) },
//            isError = field.error.isNotBlank()
//        )
//        if (field.error.isNotBlank()) {
//            Text(field.error) // error message
//        }
//    }
//}

//@Composable
//fun ProfileScreen(
//    viewModel: RoomViewModel = hiltViewModel()) {
//    val state = viewModel.state.value
//    Box(modifier = Modifier.fillMaxSize()) {
//        state.room?.let { room ->
//            LazyColumn(
//                modifier = Modifier.fillMaxSize(),
//                contentPadding = PaddingValues(20.dp)) {
//                item {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Text(
//                            text = "${room.title} (${room.id})",
//                            style = MaterialTheme.typography.h2,
//                            modifier = Modifier.weight(8f)
//                        )
//                    }
//                    Spacer(modifier = Modifier.height(15.dp))
//
//                    Spacer(modifier = Modifier.height(15.dp))
//                    Text(
//                        text = "Tags",
//                        style = MaterialTheme.typography.h3
//                    )
//                }
//            }
//        }
////        Box(modifier = Modifier.fillMaxSize()){
////            state.users?.let {
////                it.map { user ->
////                    Text(text = user.fullName)
////                }
////            }
////        }
//        if(state.error.isNotBlank()) {
//            Text(
//                text = state.error,
//                color = MaterialTheme.colors.error,
//                textAlign = TextAlign.Center,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 20.dp)
//                    .align(Alignment.Center)
//            )
//        }
//        if(state.loading) {
//            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//        }
//    }
//}

/*
* @Preview(showBackground = true)
* @Composable
* fun DefaultPreview() {
*   WhoKnowsTheme {
*       ProfileScreen()
*   }
* }
* */