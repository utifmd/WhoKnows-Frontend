package com.dudegenuine.whoknows.ux.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.dudegenuine.whoknows.ux.compose.screen.MainScreen
import com.dudegenuine.whoknows.ux.compose.screen.seperate.main.MainProps
import com.dudegenuine.whoknows.ux.vm.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    private val TAG = javaClass.simpleName
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(viewModel) {
            registerReceiver(connectionReceiver, receiver.connectionIntent)
            registerReceiver(tokenReceiver, receiver.tokenIntent)

            setContent {
                val properties = MainProps(
                    applicationContext, rememberNavController(), this, intent,
                    roomCompleteFlow.collectAsLazyPagingItems(),
                    roomsCensoredFlow.collectAsLazyPagingItems(),
                    participantsFlow.collectAsLazyPagingItems(),
                    questionsFlow.collectAsLazyPagingItems(),
                    notificationsFlow.collectAsLazyPagingItems()
                )
                MainScreen(props = properties)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        with (viewModel) {
            connectionReceiver.apply(::unregisterReceiver)
            tokenReceiver.apply(::unregisterReceiver)
        }
    }

    companion object {
        private const val INITIAL_DATA_KEY = "initial_data_key"
        fun instance(context: Context, data: String) =
            Intent(context, MainActivity::class.java).apply {
            putExtra(INITIAL_DATA_KEY, data)
        }
    }
}