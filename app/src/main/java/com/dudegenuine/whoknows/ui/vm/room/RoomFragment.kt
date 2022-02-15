package com.dudegenuine.whoknows.ui.vm.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment

/**
 * Thu, 23 Dec 2021
 * WhoKnows by utifmd
 **/
class RoomFragment(): Fragment() {

    // private val viewModel: RoomViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View = ComposeView(requireContext()).apply {

        setContent {

        }
    }
}