package com.dudegenuine.whoknows.ux.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.dudegenuine.whoknows.ux.compose.screen.MainScreen
import com.dudegenuine.whoknows.ux.vm.main.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() { //private val TAG = javaClass.simpleName
    private val viewModel: ActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(viewModel) {
            registerPrefsListener()
            registerReceiver(messagingReceiver, receiver.messagingIntent)
            registerReceiver(connectionReceiver, receiver.connectionIntent)
            setContent { MainScreen(vmMain = this, intent = intent) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        with (viewModel) {
            unregisterPrefsListener()
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