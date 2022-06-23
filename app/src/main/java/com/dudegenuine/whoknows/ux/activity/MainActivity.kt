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
            //registerPrefsListener()
            registerReceiver(messagingReceiver, receiver.messagingIntent)
            registerReceiver(connectionReceiver, receiver.connectionIntent)

            setContent {
                MainScreen(
                    props = MainProps(
                        context = applicationContext,
                        intent = intent,
                        viewModel = this,
                        router = rememberNavController(),
                        lazyPagingQuizzes = questionsFlow.collectAsLazyPagingItems(),
                        lazyPagingRoomComplete = roomCompleteFlow.collectAsLazyPagingItems(),
                        lazyPagingRoomCensored = roomsCensoredFlow.collectAsLazyPagingItems(),
                        lazyPagingParticipants = participantsFlow.collectAsLazyPagingItems(),
                        lazyPagingNotification = notificationsFlow.collectAsLazyPagingItems()
                    )
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        with (viewModel) {
            //unregisterPrefsListener()
            messagingReceiver.apply(::unregisterReceiver)
            connectionReceiver.apply(::unregisterReceiver)
        }
    }

    companion object {
        private const val INITIAL_DATA_KEY = "initial_data_key"

        fun createInstance(context: Context, data: String):
                Intent = Intent(context, MainActivity::class.java).apply {
            putExtra(INITIAL_DATA_KEY, data)
        }
    }
}