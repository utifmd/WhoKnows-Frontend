package com.dudegenuine.whoknows.ui.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GeneralTopBar(
    title: String,
    isSubmit: Boolean = false,
    submission: String? = null,
    onSubmitPressed: (() -> Unit)? = null){

    TopAppBar(
        backgroundColor = MaterialTheme.colors.primarySurface) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.padding(
                    horizontal = 16.dp),
                text = title,
                style = MaterialTheme.typography.h6)

            val onSubmitClick: () -> Unit = {
                onSubmitPressed?.let { onSubmitPressed() }
            }

            submission?.let {
                TextButton(
                    enabled = isSubmit,
                    onClick = onSubmitClick) {
                    Text(
                        text = it,
                        color = if(isSubmit) MaterialTheme.colors.primary else MaterialTheme.colors.error)
                }
            }
        }
    }
}