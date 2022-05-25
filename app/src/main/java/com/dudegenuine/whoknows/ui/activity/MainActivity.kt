package com.dudegenuine.whoknows.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.dudegenuine.whoknows.ui.compose.screen.MainScreen
import com.dudegenuine.whoknows.ui.vm.main.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() { //private val TAG = javaClass.simpleName
    private val viewModel: ActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(viewModel) {
            registerPrefsListener()
            registerReceiver(messagingServiceReceiver, messagingServiceAction)
            registerReceiver(networkServiceReceiver, networkServiceAction)
            setContent { MainScreen(vmMain = this, intent = intent) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        with (viewModel) {
            unregisterPrefsListener()
            messagingServiceReceiver.apply(::unregisterReceiver)
            networkServiceReceiver.apply(::unregisterReceiver)
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