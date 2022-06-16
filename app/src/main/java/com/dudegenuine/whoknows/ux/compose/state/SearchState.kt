package com.dudegenuine.whoknows.ux.compose.state

/**
 * Wed, 15 Jun 2022
 * WhoKnows by utifmd
 **/
sealed class SearchState {
    object User: SearchState()
    object Room: SearchState()
}
