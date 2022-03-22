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
class MainActivity: ComponentActivity() {
    private val TAG = javaClass.simpleName
    private val vmMain: ActivityViewModel by viewModels()
    

    companion object {
        const val INITIAL_DATA_KEY = "initial_data_key"

        fun createIntent(context: Context, data: String): Intent {
            return Intent(context, MainActivity::class.java)
                .apply { putExtra(INITIAL_DATA_KEY, data) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(vmMain) {
            registerReceiver(messagingServiceReceiver, messagingServiceAction)
            registerReceiver(networkServiceReceiver, networkServiceAction)
            registerPrefsListener()
        }

        onIntention()
    }

    private fun onIntention() {
        setContent { MainScreen(vmMain = vmMain) }

        /*val data = intent.getStringExtra(INITIAL_DATA_KEY) ?: ""
        //, initialPassed = data,

        if(data.isNotBlank()) Log.d(TAG, "initial data key: $data")*/
    }

    override fun onDestroy() {
        super.onDestroy()

        with (vmMain) {
            messagingServiceReceiver.apply(::unregisterReceiver)
            networkServiceReceiver.apply(::unregisterReceiver)
            unregisterPrefsListener()
        }
    }
}