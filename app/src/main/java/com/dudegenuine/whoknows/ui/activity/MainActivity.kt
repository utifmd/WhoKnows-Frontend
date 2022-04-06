package com.dudegenuine.whoknows.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.screen.MainScreen
import com.dudegenuine.whoknows.ui.vm.main.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@FlowPreview
@AndroidEntryPoint
class MainActivity: ComponentActivity() { //private val TAG = javaClass.simpleName
    private val vmMain: ActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(vmMain) {
            registerPrefsListener()
            registerReceiver(messagingServiceReceiver, messagingServiceAction)
            registerReceiver(networkServiceReceiver, networkServiceAction)
            setContent { MainScreen(vmMain = this) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        with (vmMain) {
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