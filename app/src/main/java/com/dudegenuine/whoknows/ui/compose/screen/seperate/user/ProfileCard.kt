package com.dudegenuine.whoknows.ui.compose.screen.seperate.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.component.GeneralCardView
import com.dudegenuine.whoknows.ui.compose.component.GeneralImage
import com.dudegenuine.whoknows.ui.theme.SmoothBackground

/**
 * Sat, 15 Jan 2022
 * WhoKnows by utifmd
 **/

@ExperimentalCoilApi
@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    colorBorder: Color? = null,
    name: String,
    desc: String,
    data: String) {

    GeneralCardView(colorBorder = colorBorder) {
        Row(modifier.padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colors.surface)) {

            Surface(modifier.size(50.dp),
                shape = CircleShape,
                color = SmoothBackground) {

                GeneralImage(modifier.fillMaxSize(),
                    data = data,
                    contentScale = ContentScale.Crop,
                    placeholder = {
                        Icon(Icons.Default.Person,
                            modifier = modifier.fillMaxSize().padding(4.dp),
                            tint = MaterialTheme.colors.secondaryVariant, contentDescription = null
                        )
                    }
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)) {
                Text(name, fontWeight = FontWeight.Bold)

                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(desc, style = MaterialTheme.typography.body2)
                }
            }
        }
    }
}