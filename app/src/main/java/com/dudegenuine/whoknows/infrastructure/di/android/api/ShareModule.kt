package com.dudegenuine.whoknows.infrastructure.di.android.api

import android.content.Context
import android.content.Intent
import com.dudegenuine.local.api.IShareLauncher
import com.dudegenuine.whoknows.R

/**
 * Fri, 11 Mar 2022
 * WhoKnows by utifmd
 **/
class ShareModule(
    private val context: Context): IShareLauncher {
    private val intent = Intent.createChooser(Intent(), context.getString(R.string.share))

    override fun launch(data: String) {
        with (intent) {
            putExtra(Intent.EXTRA_TITLE, context.getString(R.string.app_name))
            putExtra(Intent.EXTRA_TEXT, data) // data = contentUri

            type = "text/plain"
            action = Intent.ACTION_SEND
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        intent.apply(context::startActivity)
    }
}