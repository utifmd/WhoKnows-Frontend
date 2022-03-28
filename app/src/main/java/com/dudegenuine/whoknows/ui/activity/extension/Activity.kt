package com.dudegenuine.whoknows.ui.activity.extension

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * Sat, 12 Feb 2022
 * WhoKnows by utifmd
 **/

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

/*private fun splashScreen(savedInstanceState: Bundle?, onFinished: () -> Unit){
        val logoCrossFadeDurationMillis = 5_000
        val spacingAfterFadeDurationMillis = 500
        // Checks if the splash screen was displayed before
        // ->SavedInstanceState is not null after recreation!
        val splashWasDisplayed = savedInstanceState != null
        if(!splashWasDisplayed){
            // 1 - Start fading out the logo
            (window.decorView.background
                    as TransitionDrawable).startTransition(
                logoCrossFadeDurationMillis
            )
            // 2 = As we can't register a listener to be
            // notified when the transition drawable finishes,
            // launches a coroutine that blocks while animation
            // is being performed and sets the content view
            lifecycleScope.launch {
                // Time between the animations
                delay(logoCrossFadeDurationMillis.toLong() + spacingAfterFadeDurationMillis)
                window.decorView.background =
                    getDrawable(R.drawable.splash_background)
                onFinished()//setContentView(R.layout.activity_main)
            }
        }else{
            // Splash was shown before, no need to animate it.
            // 1 - Updates the window background (if needed)
            window.decorView.background =
                getDrawable(R.drawable.splash_background)
            // 2 - Sets the content view instantly
            onFinished()//setContentView(R.layout.activity_main)
        }
    }*/