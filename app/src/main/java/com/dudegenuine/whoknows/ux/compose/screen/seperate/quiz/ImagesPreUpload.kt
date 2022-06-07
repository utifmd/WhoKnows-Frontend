package com.dudegenuine.whoknows.ux.compose.screen.seperate.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.dudegenuine.model.common.ImageUtil.asBitmap

@Composable
fun ImagesPreUpload(
    modifier: Modifier = Modifier,
    images: List<ByteArray>,
    onAddPressed: () -> Unit,
    onRemovePressed: (Int) -> Unit) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)){
        itemsIndexed(images) { idx, byteArray ->
            Box(
                contentAlignment = Alignment.TopEnd) {

                Image( //painter = rememberImagePainter(data = uri),
                    bitmap = asBitmap(byteArray).asImageBitmap(),
                    contentDescription = "uri $idx",
                    modifier = modifier.size(128.dp)
                )

                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "iconClose",
                    tint = MaterialTheme.colors.surface,
                    modifier = modifier
                        .clip(shape = CircleShape)
                        .padding(4.dp)
                        .background(MaterialTheme.colors.primary)
                        .size(24.dp)
                        .clickable { onRemovePressed(idx) }
                )
            }
        }
        if (images.size < 3) item {
            Icon(Icons.Default.AddCircleOutline,
                tint = MaterialTheme.colors.primary, contentDescription = null,
                modifier = modifier.size(35.dp).padding(5.dp).clickable(
                    enabled = images.size < 3,
                    onClick = onAddPressed
                )
            )
        }
    }
}