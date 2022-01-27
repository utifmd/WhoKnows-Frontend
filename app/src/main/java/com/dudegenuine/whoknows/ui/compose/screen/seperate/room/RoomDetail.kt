package com.dudegenuine.whoknows.ui.compose.screen.seperate.room

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dudegenuine.whoknows.ui.compose.component.misc.FieldTag
import com.dudegenuine.whoknows.ui.compose.screen.ErrorScreen
import com.dudegenuine.whoknows.ui.compose.screen.LoadingScreen
import com.dudegenuine.whoknows.ui.presenter.room.RoomViewModel
import okhttp3.internal.http.toHttpDateString

/**
 * Thu, 27 Jan 2022
 * WhoKnows by utifmd
 **/
@Composable
fun RoomDetail(
    modifier: Modifier = Modifier,
    isOwn: Boolean = false,
    viewModel: RoomViewModel = hiltViewModel()) {
    val state = viewModel.state

    Scaffold(
        modifier = modifier.fillMaxSize()) {
        if (state.loading){
            LoadingScreen()
        }

        state.room?.let { model ->
            Column {
                Text(text = model.title, style = MaterialTheme.typography.h6)

                Text(text = model.description, style = MaterialTheme.typography.body1)

                Spacer(modifier = Modifier.height(16.dp))

                FieldTag(key = "Duration", value = model.minute.toString())
                FieldTag(key = "Since", value = model.createdAt.toHttpDateString())

                model.updatedAt?.let { date ->
                    FieldTag(key = "Expired At", value = date.toString())
                }
            }
        }

        if (state.error.isNotBlank()){
            ErrorScreen(message = state.error)
        }
    }

    /*val userId: String
    var title: String
    var description: String
    var minute: Int
    var expired: Boolean
    var createdAt: Date
    var updatedAt: Date?*/
}