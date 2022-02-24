package com.dudegenuine.whoknows.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    private val TAG = javaClass.simpleName
    private val mainVm: ActivityViewModel by viewModels()

    companion object {
        const val INITIAL_DATA_KEY = "initial_data_key"

        fun createIntent(context: Context, data: String): Intent {
            return Intent(context, MainActivity::class.java)
                .apply { putExtra(INITIAL_DATA_KEY, data) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainVm.apply { registerReceiver(
                messagingServiceReceiver, messagingServiceAction) }

        onIntention()
    }

    private fun onIntention() {
        val data = intent.getStringExtra(INITIAL_DATA_KEY) ?: ""

        setContent {
            MainScreen(initialPassed = data)
        }

        Log.d(TAG, "initial data key: $data")
    }

    override fun onDestroy() {
        super.onDestroy()

        mainVm.messagingServiceReceiver
            .apply(::unregisterReceiver)
    }
}