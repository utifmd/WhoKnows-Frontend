package com.dudegenuine.whoknows.ux.compose.component.misc

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dudegenuine.whoknows.R

/**
 * Tue, 15 Mar 2022
 * WhoKnows by utifmd
 **/
@Composable
fun FrontLiner(
    modifier: Modifier = Modifier) {

    Column(modifier.padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Icon(Icons.Filled.FactCheck,
            tint = MaterialTheme.colors.primary,
            modifier = modifier.size(124.dp), contentDescription = null)

        Text(stringResource(R.string.app_name),
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.W500
        )
    }
}