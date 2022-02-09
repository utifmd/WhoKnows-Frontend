package com.dudegenuine.whoknows

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.screen.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalCoilApi @ExperimentalMaterialApi @ExperimentalFoundationApi
class MainActivity: ComponentActivity() {
    private val TAG = javaClass.simpleName

    companion object {
        private const val INITIAL_TIME_KEY = "initial_time_key"

        fun createIntent(context: Context, time: String): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(INITIAL_TIME_KEY, time)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}