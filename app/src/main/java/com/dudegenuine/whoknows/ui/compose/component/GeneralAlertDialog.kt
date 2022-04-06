package com.dudegenuine.whoknows.ui.compose.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        fun onDismissedPressed() { onDismissed?.invoke() ?:
            viewModel.onDialogStateChange(null) }
        fun onButtonPressed() {
            onSubmitted?.invoke()
            onDismissedPressed()
        }

        val message = buildAnnotatedString {
            append(text)

            withStyle(SpanStyle(
                MaterialTheme.colors.primaryVariant,
                fontStyle = FontStyle.Italic, fontSize = 11.sp)) {

                disclaimer?.let { data ->
                    append('\n')
                    append('\n')
                    append("Disclaimer:")
                    append('\n')
                    append(data)
                }
            }
        }
        AlertDialog(::onDismissedPressed,
            modifier = Modifier.padding(horizontal = 24.dp),
            title = { Text(title) },
            text = { Text(message) },
            confirmButton = {

                TextButton(::onButtonPressed,
                    enabled = onSubmitted != null) {

                    Text(buttonText)
                }
            }
        )
    }
}