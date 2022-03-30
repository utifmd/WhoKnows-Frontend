package com.dudegenuine.whoknows.ui.compose.component.misc

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.BuildConfig
import com.dudegenuine.whoknows.ui.compose.component.GeneralImage
import com.dudegenuine.whoknows.ui.vm.file.FileViewModel

/**
 * Tue, 15 Mar 2022
 * WhoKnows by utifmd
 **/
@ExperimentalCoilApi
@Composable
fun ImageViewer(fileId: String,
    modifier: Modifier = Modifier,
    viewModel: FileViewModel = hiltViewModel(),
    onBackPressed: (() -> Unit)? = null) {
    var scale by remember { mutableStateOf(1f) }

    Box(modifier.fillMaxSize()) {
        GeneralImage(
            modifier
                .align(Alignment.Center)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale
                )
                .pointerInput(Unit) {
                    detectTransformGestures { _, _, zoom, _ ->
                        scale = when {
                            scale < 0.5f -> 0.5f
                            scale > 3f -> 3f
                            else -> scale * zoom
                        }
                    }
                },
            data = "${BuildConfig.BASE_URL}/files/$fileId",
            contentScale = ContentScale.FillWidth){

            Box(modifier.fillMaxSize(),
                contentAlignment = Alignment.Center){

                CircularProgressIndicator()
            }
        }

        Row(
            modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {

            IconButton({ onBackPressed?.invoke() }) {
                Icon(Icons.Filled.ArrowBack,
                    tint = MaterialTheme.colors.primaryVariant,
                    contentDescription = null)
            }
            IconButton({ viewModel.onSharePressed(fileId) }) {
                Icon(Icons.Filled.Share,
                    tint = MaterialTheme.colors.primaryVariant,
                    contentDescription = null)
            }
        }
    }
}