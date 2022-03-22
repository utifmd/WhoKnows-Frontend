package com.dudegenuine.whoknows.ui.compose.component

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi

@Composable
@ExperimentalCoilApi
fun GeneralPicture(
    modifier: Modifier = Modifier, data: Any,
    onGeneralImagePressed:((String?) -> Unit)? = null,
    onPreviewPressed:(() -> Unit)? = null,
    onChangePressed:(() -> Unit)? = null,
    onCheckPressed:(() -> Unit)? = null){
    var editable by remember { mutableStateOf(false) }
    val toggle: () -> Unit = { editable = !editable }

    Surface(modifier.size(120.dp),
        shape = CircleShape,
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)) {

        Box(if (onGeneralImagePressed != null) modifier.fillMaxSize()
            else modifier.fillMaxSize().clickable(onClick = toggle)) {

            if(editable) {
                Column(modifier.fillMaxSize()
                    .background(MaterialTheme.colors.secondaryVariant),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly) {

                    if (data is Bitmap){
                        Icon(Icons.Default.Check, contentDescription = null,
                            tint = MaterialTheme.colors.onPrimary,
                            modifier = modifier.clickable(
                                enabled = onCheckPressed != null,
                                onClick = { onCheckPressed?.invoke() }
                            ),
                        )
                    }

                    Icon(Icons.Default.Visibility,
                        tint = MaterialTheme.colors.onPrimary, contentDescription = null,
                        modifier = modifier.clickable (
                            enabled = onPreviewPressed != null,
                            onClick = {
                                onPreviewPressed?.invoke()
                                toggle()
                            }
                        ),
                    )

                    Icon(Icons.Default.Photo,
                        tint = MaterialTheme.colors.onPrimary, contentDescription = null,
                        modifier = modifier.clickable (
                            enabled = onChangePressed != null,
                            onClick = {
                                onChangePressed?.invoke()
                                toggle()
                            }
                        ),
                    )
                }
            } else {
                GeneralImage(modifier.fillMaxSize(),
                    data = data, onPressed = onGeneralImagePressed,
                    contentScale = ContentScale.Crop,
                    placeholder = {
                        Icon(Icons.Default.Person,
                            modifier = modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            tint = MaterialTheme.colors.primary,
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }
}