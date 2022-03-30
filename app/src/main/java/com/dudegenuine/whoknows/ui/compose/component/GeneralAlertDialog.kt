package com.dudegenuine.whoknows.ui.compose.component

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import com.dudegenuine.whoknows.ui.compose.state.DialogState
import com.dudegenuine.whoknows.ui.vm.main.IActivityViewModel

/**
 * Wed, 30 Mar 2022
 * WhoKnows by utifmd
 **/
@Composable
fun GeneralAlertDialog(viewModel: IActivityViewModel) {
    viewModel.dialogState?.apply {
        val buttonText = button
            .replaceFirstChar { it.uppercase() }
        fun onDismissedPressed() {
            onDismissed?.invoke() ?: viewModel.onDialogStateChange(DialogState()) }
        fun onButtonPressed() {
            onSubmitted?.invoke()
            onDismissed?.invoke()
        }

        AlertDialog(::onDismissedPressed,
            title = { Text(title) },
            text = { Text(disclaimer ?: text) },
            confirmButton = {
                TextButton(::onButtonPressed,
                    enabled = onSubmitted != null) {

                    Text(buttonText)
                }
            }
        )
    }
}