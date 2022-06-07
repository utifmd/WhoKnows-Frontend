package com.dudegenuine.repository.contract.dependency.local

import android.content.Intent

interface ITimerLauncher {
    fun start(time: Double): Intent
    fun stop()
}