package com.dudegenuine.whoknows.ux.compose.component

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.dudegenuine.whoknows.ux.compose.state.ScreenState
import com.dudegenuine.whoknows.ux.vm.main.IMainViewModel

/**
 * Wed, 30 Mar 2022
 * WhoKnows by utifmd
 **/
@Composable
fun GeneralAlertDialog(
    modifier: Modifier = Modifier, viewModel: IMainViewModel) {
    val state by viewModel.screenState.collectAsState(initial = null)

    if(state is ScreenState.AlertDialog) {
        (state as ScreenState.AlertDialog).state?.apply {
            val buttonText = button.replaceFirstChar { it.uppercase() }

            fun onDismissedPressed() {
                onDismissed?.invoke() ?: viewModel.onScreenStateChange(
                    ScreenState.AlertDialog(null)
                )
            }

            fun onButtonPressed() {
                onSubmitted?.invoke()
                onDismissedPressed()
            }

            val message = buildAnnotatedString {
                append(text)

                withStyle(
                    SpanStyle(
                        MaterialTheme.colors.primaryVariant,
                        fontStyle = FontStyle.Italic, fontSize = 11.sp
                    )
                ) {

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
                modifier = modifier/*.padding(horizontal = 24.dp)*/,
                title = { Text(title) },
                text = { Text(message) },
                confirmButton = {

                    TextButton(
                        ::onButtonPressed,
                        enabled = onSubmitted != null
                    ) {

                        Text(buttonText)
                    }
                }
            )

        }
    }
}