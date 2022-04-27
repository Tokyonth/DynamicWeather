package com.tokyonth.weather.utils.event

import android.os.Handler
import android.os.Looper

enum class ThreadMode {
    ORIGIN, MAIN
}

object ThreadManager {

    private val mainHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    fun runOnMainThread(block: () -> Unit) {
        if (isMainThread()) {
            block()
        } else {
            mainHandler.post(block)
        }
    }

    private fun isMainThread(): Boolean {
        return Looper.myLooper() == Looper.getMainLooper()
    }

}
