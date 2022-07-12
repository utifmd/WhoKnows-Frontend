package com.dudegenuine.whoknows.ux.compose.screen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.dudegenuine.model.Room
import com.dudegenuine.model.User
import com.dudegenuine.whoknows.R
import com.dudegenuine.whoknows.ux.compose.component.misc.LazyStatePaging
import com.dudegenuine.whoknows.ux.compose.screen.seperate.room.RoomItem
import com.dudegenuine.whoknows.ux.compose.screen.seperate.user.ProfileCard
import com.dudegenuine.whoknows.ux.compose.state.SearchState
import com.dudegenuine.whoknows.ux.vm.search.SearchViewModel

/**
 * Sun, 12 Jun 2022
 * WhoKnows by utifmd
 **/
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier, viewModel: SearchViewModel) {
    val lazyPagingItems = viewModel.pagingFlow.collectAsLazyPagingItems()
    val focusRequester = remember { FocusRequester() }

    Scaffold(modifier.fillMaxSize(), topBar = {
        BodyTopBar(modifier, focusRequester, viewModel) }){
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            stickyHeader {
                Row(verticalAlignment = Alignment.CenterVertically){
                    Text("Searching for:", style = MaterialTheme.typography.caption)
                    Spacer(modifier.size(12.dp))
                    Chip(viewModel::onChipUserPressed,
                        border = if (viewModel.type is SearchState.User) ChipDefaults.outlinedBorder else null){
                        Text("user", style = MaterialTheme.typography.caption)
                    }
                    Spacer(modifier.size(8.dp))
                    Chip(viewModel::onChipRoomPressed,
                        border = if (viewModel.type is SearchState.Room) ChipDefaults.outlinedBorder else null){
                        Text("class", style = MaterialTheme.typography.caption)
                    }
                }
            }
            item { LazyStatePaging(lazyPagingItems) }
            items(lazyPagingItems){ search ->
                search?.let { model ->
                    when (model.data){
                        is User.Censored -> with(model.data as User.Censored){
                            ProfileCard(name = fullName, desc = "@$username", data = profileUrl)
                        }
                        is Room.Censored -> with(model.data as Room.Censored){
                            RoomItem(model = this)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun BodyTopBar(
    modifier: Modifier = Modifier, focusRequester: FocusRequester, viewModel: SearchViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val clear by remember(viewModel.field.text) { mutableStateOf(viewModel.field.text.isNotBlank()) }

    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onBackground,
        elevation = (0.5).dp){
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(viewModel::onBackPressed) {
                Icon(Icons.Filled.ArrowBack,
                    tint = MaterialTheme.colors.onSurface, contentDescription = null
                )
            }
            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                maxLines = 1,
                singleLine = true,
                value = viewModel.field.text /*text*/,
                onValueChange = viewModel::onSearchTextChange,
                placeholder = { Text(stringResource(R.string.search_placholder)) },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions {
                    viewModel.onSearchButtonPressed()
                    keyboardController?.hide()
                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                    cursorColor = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                ),
                /*leadingIcon = {
                    IconButton(viewModel::onSearchTextFlowChange) {
                        Icon(Icons.Default.Search,
                            tint = MaterialTheme.colors.onSurface, contentDescription = null
                        )
                    }
                },*/
                trailingIcon = {
                    AnimatedVisibility(
                        visible = clear,
                        enter = fadeIn(),
                        exit = fadeOut()) {
                        IconButton(viewModel::onClearFieldPressed) {
                            Icon(
                                Icons.Default.Clear,
                                tint = MaterialTheme.colors.onSurface, contentDescription = null
                            )
                        }
                    }
                }
            )
        }
    }
    LaunchedEffect(Unit){
        focusRequester.requestFocus()
    }
}