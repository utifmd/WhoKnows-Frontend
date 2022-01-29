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
/* Backend
* Room +
* - field private
*
* Participant +
* - related to user by userId (OneToOne)
*
*
* Frontend
* - Paging
* - Limitation
* */