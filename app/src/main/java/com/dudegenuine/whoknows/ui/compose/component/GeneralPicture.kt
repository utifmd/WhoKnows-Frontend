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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi

@Composable
@ExperimentalCoilApi
fun GeneralPicture(
    modifier: Modifier = Modifier,
    data: Any,
    onChangePressed:(() -> Unit)? = null,
    onCheckPressed:(() -> Unit)? = null){

    val toggle = remember { mutableStateOf(false) }

    val onChangeClicked: () -> Unit = {
        onChangePressed?.let {
            onChangePressed().also {
                toggle.value = !toggle.value
            }
        }
    }
    val onCheckClicked: () -> Unit = {
        if (onCheckPressed != null) onCheckPressed()
    }

    Surface(
        modifier = modifier.size(120.dp),
        shape = CircleShape,
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)) {

        Box(
            modifier = modifier
                .fillMaxSize()
                .clickable { toggle.value = !toggle.value }) {
            GeneralImage(
                modifier = modifier.fillMaxSize(),
                data = data,
                placeholder = {
                    Icon(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        imageVector = Icons.Default.Person, tint = MaterialTheme.colors.primary,
                        contentDescription = null
                    )
                }
            )

            if(toggle.value){
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.secondaryVariant),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly) {

                    if (data is Bitmap){
                        Icon(
                            imageVector = Icons.Default.Check,
                            tint = MaterialTheme.colors.onPrimary,
                            modifier = modifier.clickable(
                                onClick = onCheckClicked
                            ),
                            contentDescription = null,
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.Photo,
                        tint = MaterialTheme.colors.onPrimary,
                        modifier = modifier.clickable(
                            onClick = onChangeClicked
                        ),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}