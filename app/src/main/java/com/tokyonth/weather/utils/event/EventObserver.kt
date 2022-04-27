package com.tokyonth.weather.utils.event

fun interface EventObserver<T : EVENT> {
    fun onEvent(event: T)
}

@Suppress("UNCHECKED_CAST")
internal fun <T : EVENT> EventObserver<T>.dispatchEvent(event: EVENT, threadMode: ThreadMode) {
    (event as? T)?.let {
        if (threadMode == ThreadMode.MAIN) {
            ThreadManager.runOnMainThread {
                onEvent(it)
            }
        } else {
            onEvent(it)
        }
    }
}
