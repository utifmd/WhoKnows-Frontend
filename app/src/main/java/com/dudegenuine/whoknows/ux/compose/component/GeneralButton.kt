package com.dudegenuine.whoknows.ux.compose.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Tue, 18 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun GeneralButton(
    modifier: Modifier = Modifier,
    modifierFillMaxWidth: Boolean = false,
    label: String,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    isLoading: Boolean = false,
    onClick: () -> Unit) {
    Button(
        modifier = if (modifierFillMaxWidth)
            modifier.fillMaxWidth() else modifier,
        enabled = enabled,
        shape = MaterialTheme.shapes.small.copy(
            topStart = CornerSize(0.dp), topEnd = CornerSize(0.dp)),
        onClick = onClick) {
        if (isLoading){
            CircularProgressIndicator(
                modifier = modifier.size(16.dp),
                color = MaterialTheme.colors.secondaryVariant,
                strokeWidth = (1.5).dp
            )
            Spacer(modifier.size(ButtonDefaults.IconSpacing))
        }
        if (leadingIcon != null){
            Icon(imageVector = leadingIcon, contentDescription = null)
            Spacer(modifier.size(ButtonDefaults.IconSpacing))
        }
        Text(label)
        if (trailingIcon != null){
            Spacer(modifier.size(ButtonDefaults.IconSpacing))
            Icon(imageVector = trailingIcon, contentDescription = null)
        }
    }
}