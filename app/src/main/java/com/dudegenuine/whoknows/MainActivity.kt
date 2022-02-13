package com.dudegenuine.whoknows

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.screen.MainScreen
import com.dudegenuine.whoknows.ui.presenter.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalCoilApi @ExperimentalMaterialApi @ExperimentalFoundationApi
class MainActivity: ComponentActivity() {
    private val TAG = javaClass.simpleName
    private val setUpViewModel: ActivityViewModel by viewModels()

    companion object {
        const val INITIAL_DATA_KEY = "initial_data_key"

        fun createIntent(context: Context, data: String): Intent {
            val intent = Intent(context, MainActivity::class.java).apply {
                putExtra(INITIAL_DATA_KEY, data)
            }

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpViewModel
        val data = intent.getStringExtra(INITIAL_DATA_KEY)
        Log.d(TAG, "onCreate: $data")

        setContent {
            MainScreen()
        }
    }
}