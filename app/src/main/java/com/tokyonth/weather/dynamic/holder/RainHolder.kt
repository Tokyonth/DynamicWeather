package com.tokyonth.weather.dynamic.holder

import android.graphics.Color

import com.tokyonth.weather.dynamic.BaseHolder

class RainHolder(
    private var x: Float,
    private val rainWidth: Float,
    minRainHeight: Float,
    maxRainHeight: Float,
    private val maxY: Float,
    speed: Float
) : BaseHolder() {

    private var v: Float = 0F
    private var curTime: Float
    private var rainColor: Int = 0
    private var rainHeight: Float = 0F

    init {
        rainHeight = getRandom(minRainHeight, maxRainHeight)
        rainColor = Color.argb((getRandom(0.1F, 0.5F) * 255f).toInt(), 0xFF, 0xFF, 0xFF)
        v = speed * getRandom(0.9f, 1.1f)
        curTime = getRandom(0F, maxY / v)
    }

    fun updateRandom(drawable: RainDrawable, mAlpha: Float) {
        curTime += 0.025F
        val curY = curTime * v
        if (curY - rainHeight > maxY) {
            curTime = 0F
        }
        drawable.apply {
            setColor(Color.argb((Color.alpha(rainColor) * mAlpha).toInt(), 0xFF, 0xFF, 0xFF))
            setStrokeWidth(rainWidth)
        }
        drawable.setLocation(x, curY, rainHeight)
    }

}
