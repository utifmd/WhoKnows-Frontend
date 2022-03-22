package com.dudegenuine.whoknows.ui.compose.component

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter

@ExperimentalCoilApi
@Composable
fun GeneralImage(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    data: Any, onPressed: ((String?) -> Unit)? = null,
    placeholder: @Composable () -> Unit) {
    val painter = rememberImagePainter(data = data)

    Box(modifier.fillMaxSize()) {
        when(data){
            is Bitmap -> Image(
                bitmap = data.asImageBitmap(),
                contentDescription = contentDescription,
                alignment = alignment,
                contentScale = contentScale,
                alpha = alpha,
                colorFilter = colorFilter,
                modifier = modifier.fillMaxSize()
            )
            else -> {
                val fileId = if (data is String) data.substringAfterLast("/") else null
                Image(
                    painter = painter,
                    contentDescription = contentDescription,
                    alignment = alignment,
                    contentScale = contentScale,
                    alpha = alpha,
                    colorFilter = colorFilter,
                    modifier = if(onPressed != null) modifier.fillMaxSize().clickable(
                        onClick = {
                            onPressed.invoke(fileId)
                        }
                    ) else modifier.fillMaxSize()
                )

                when (painter.state) { /*is ImagePainter.State.Empty, is ImagePainter.State.Success, -> {}*/
                    is ImagePainter.State.Loading,
                    is ImagePainter.State.Error -> placeholder()
                    else -> {}
                }
            }
        }

        /*AnimatedVisibility(
            visible = when (painter.state) {
                is ImagePainter.State.Empty,
                is ImagePainter.State.Success,
                -> false
                is ImagePainter.State.Loading,
                is ImagePainter.State.Error,
                -> true
            }
        ) {
            placeholder()
        }*/
    }
}