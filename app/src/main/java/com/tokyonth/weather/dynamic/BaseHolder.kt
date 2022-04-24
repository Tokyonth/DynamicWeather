package com.tokyonth.weather.dynamic

import com.tokyonth.weather.utils.RandomUtils

abstract class BaseHolder {

    private var width: Int = 0
    private var height: Int = 0

    fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    fun getMaxWidth(): Float {
        return width.toFloat()
    }

    fun getMaxHeight(): Float {
        return height.toFloat()
    }

    fun getRandom(min: Float, max: Float): Float {
        return RandomUtils.getRandom(min, max)
    }

    fun convertAlphaColor(percent: Float, originalColor: Int): Int {
        val newAlpha = (percent * 255).toInt() and 0xFF
        return newAlpha shl 24 or (originalColor and 0xFFFFFF)
    }

}
