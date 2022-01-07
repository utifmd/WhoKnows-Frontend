package com.dudegenuine.whoknows

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Thu, 02 Dec 2021
 * WhoKnows by utifmd
 **/

@HiltAndroidApp
class WhoKnows: Application()

// TODO:
//  - multiple upload
//  - compress not decreased
//  - separate viewModel resource state
//  - trim useCases hilt module