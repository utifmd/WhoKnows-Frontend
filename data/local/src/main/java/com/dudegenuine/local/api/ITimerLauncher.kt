package com.dudegenuine.local.api

import android.content.Intent

interface ITimerLauncher {
    fun start(time: Double): Intent
    fun stop()
}