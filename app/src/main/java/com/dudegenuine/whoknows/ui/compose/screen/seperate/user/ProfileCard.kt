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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.dudegenuine.whoknows.ui.compose.component.GeneralCardView
import com.dudegenuine.whoknows.ui.compose.component.GeneralImage

/**
 * Sat, 15 Jan 2022
 * WhoKnows by utifmd
 **/

@ExperimentalCoilApi
@Composable
fun ProfileCard(
    modifier: Modifier = Modifier, name: String, desc: String, data: String) {

    GeneralCardView {
        Row(
            modifier.padding(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colors.surface)) {

            Surface(
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)) {

                GeneralImage(
                    modifier = modifier.fillMaxSize(),
                    data = data,
                    placeholder = {
                        Icon(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(4.dp),
                            imageVector = Icons.Default.Person,
                            tint = MaterialTheme.colors.secondaryVariant, contentDescription = null
                        )
                    }
                )
                /*Image(
                    modifier = modifier.fillMaxSize(),
                    painter = rememberImagePainter(
                        data = url
                    ),
                    contentDescription = null
                )*/
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