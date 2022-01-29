package com.dudegenuine.whoknows.ui.compose.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun GeneralTopBar(
    modifier: Modifier = Modifier,
    title: String,
    leads: Any? = null,
    light: Boolean = true,
    tails: ImageVector? = null,
    onTailPressed: (() -> Unit) ? = null,
    submitEnable: Boolean = false,
    submitLoading: Boolean = false,
    submitLabel: String? = null,
    onSubmitPressed: (() -> Unit)? = null){

    val onSubmitClick: () -> Unit = {
        onSubmitPressed?.let { onSubmitPressed() }
    }

    val onTailClicked: () -> Unit = {
        onTailPressed?.let { onTailPressed() }
    }

    TopAppBar(
        elevation = (0.5).dp,
        backgroundColor = if (light) MaterialTheme.colors.surface
            else MaterialTheme.colors.primary) {

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {

            Row(
                verticalAlignment = Alignment.CenterVertically){

                when (leads) {
                    is ImageVector -> {
                        Icon(
                            modifier = modifier.padding(start = 16.dp, end = 12.dp),
                            imageVector = leads, contentDescription = null,
                            tint = if (light) MaterialTheme.colors.primaryVariant.copy(alpha = 0.8f)
                                else MaterialTheme.colors.onPrimary
                        )
                    }
                }
                Text(
                    modifier = modifier.padding(
                        horizontal = if (leads != null) 0.dp else 16.dp
                    ),
                    text = title,
                    color = if (light) MaterialTheme.colors.primaryVariant.copy(alpha = 0.8f)
                        else MaterialTheme.colors.surface,
                    style = MaterialTheme.typography.h6
                )
            }

            if(tails != null){
                IconButton(
                    onClick = onTailClicked) {

                    Icon(
                        imageVector = tails,
                        tint = if (light) MaterialTheme.colors.primaryVariant.copy(alpha = 0.8f)
                            else MaterialTheme.colors.onPrimary,
                        contentDescription = null)
                }
            }

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