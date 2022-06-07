package com.dudegenuine.whoknows.ux.compose.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Tue, 18 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun GeneralButton(
    modifier: Modifier = Modifier,
    label: String,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit) {
    Button(
        enabled = enabled,
        onClick = onClick) {
        if (isLoading){
            CircularProgressIndicator(
                modifier = modifier.size(16.dp),
                color = MaterialTheme.colors.secondaryVariant,
                strokeWidth = (1.5).dp
            )
            Spacer(modifier = modifier.width(8.dp))
        }
        Text(text = label)
    }
}