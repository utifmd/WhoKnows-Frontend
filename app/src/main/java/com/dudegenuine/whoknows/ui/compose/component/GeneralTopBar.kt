package com.dudegenuine.whoknows.ui.compose.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GeneralTopBar(
    modifier: Modifier = Modifier,
    title: String,
    submitEnable: Boolean = false,
    submitLoading: Boolean = false,
    submitLabel: String? = null,
    onSubmitPressed: (() -> Unit)? = null){

    val onSubmitClick: () -> Unit = {
        onSubmitPressed?.let { onSubmitPressed() }
    }

    TopAppBar(
        elevation = (0.5).dp,
        backgroundColor = MaterialTheme.colors.surface) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = modifier.padding(
                    horizontal = 16.dp),
                text = title,
                color = MaterialTheme.colors.primaryVariant, //fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.h6)

            submitLabel?.let {
                TextButton(
                    enabled = submitEnable,
                    onClick = onSubmitClick) {

                    if (submitLoading){
                        CircularProgressIndicator(
                            modifier = modifier.size(16.dp),
                            color = MaterialTheme.colors.error,
                            strokeWidth = (1.5).dp
                        )
                        Spacer(modifier = modifier.width(8.dp))
                    }

                    Text(
                        text = it,
                        color = if(submitEnable)
                            MaterialTheme.colors.primary
                        else
                            MaterialTheme.colors.error)
                }
            }
        }
    }
}