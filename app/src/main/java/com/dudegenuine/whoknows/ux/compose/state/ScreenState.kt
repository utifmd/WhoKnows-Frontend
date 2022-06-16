package com.dudegenuine.whoknows.ux.compose.state

import androidx.compose.material.SnackbarDuration
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.dudegenuine.whoknows.ux.compose.model.Dialog

/**
 * Wed, 01 Jun 2022
 * WhoKnows by utifmd
 **/
sealed class ScreenState {
    object Navigate {
        data class To(
            val route: String,
            val option: NavOptions? = null,
            val extras: Navigator.Extras? = null
        ): ScreenState()
        object Back: ScreenState()
    }
    data class Toast(
        val message: String,
        val duration: Int = android.widget.Toast.LENGTH_SHORT
    ): ScreenState()
    data class SnackBar(
        val message: String,
        val label: String? = null,
        val duration: SnackbarDuration = SnackbarDuration.Short
    ): ScreenState()
    data class AlertDialog(
        val state: Dialog?
    ): ScreenState()
}
