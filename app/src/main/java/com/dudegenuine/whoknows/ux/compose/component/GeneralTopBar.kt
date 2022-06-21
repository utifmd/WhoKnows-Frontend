package com.dudegenuine.whoknows.ux.compose.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun GeneralTopBar(
    modifier: Modifier = Modifier,
    title: String,
    light: Boolean = true,
    leads: Any? = null,
    onLeadsPressed: (() -> Unit)? = null,
    tails: ImageVector? = null,
    tailsTint: Color? = null,
    onTailPressed: (() -> Unit) ? = null,
    submitEnable: Boolean = false,
    submitLoading: Boolean = false,
    submitLabel: String? = null,
    onSubmitPressed: (() -> Unit)? = null){

    TopAppBar(
        elevation = (0.5).dp,
        backgroundColor = if (light) MaterialTheme.colors.surface
            else MaterialTheme.colors.primary,
        title = {
            Text(title,
                color = if (light) MaterialTheme.colors.onSurface
                    else MaterialTheme.colors.onPrimary,
            )
        },
        navigationIcon = if (leads is ImageVector) {{
            IconButton({ onLeadsPressed?.invoke() }) {
                Icon(
                    leads, contentDescription = null,
                    tint = if (light) MaterialTheme.colors.onSurface
                else MaterialTheme.colors.onPrimary)}}}
            else null,
        actions =  {
            tails?.let {
                IconButton({ onTailPressed?.invoke() }, enabled = onTailPressed != null) {

                    Icon(tails,
                        tint = if (light) tailsTint ?: MaterialTheme.colors.onSurface
                            else MaterialTheme.colors.onPrimary,
                        contentDescription = null)
                }
            }

            submitLabel?.let {
                TextButton({ onSubmitPressed?.invoke() },
                    enabled = submitEnable) {

                    if (submitLoading){
                        CircularProgressIndicator(modifier.size(16.dp),
                            color = MaterialTheme.colors.error,
                            strokeWidth = (1.5).dp
                        )
                        Spacer(modifier.width(8.dp))
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
    )
}