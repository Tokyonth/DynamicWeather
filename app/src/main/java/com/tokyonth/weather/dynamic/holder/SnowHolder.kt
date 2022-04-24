package com.tokyonth.weather.dynamic.holder

import android.graphics.drawable.GradientDrawable

import kotlin.math.roundToInt

import com.tokyonth.weather.dynamic.BaseHolder

class SnowHolder(
    private var x: Float,
    private val snowSize: Float, // [0,1]
    private val maxY: Float,
    averageSpeed: Float
) : BaseHolder() {

    private var v: Float = 0F
    private var maxTime = 0F
    private var curTime: Float

    init {
        v = averageSpeed * getRandom(0.85F, 1.15F)
        maxTime = maxY / v
        curTime = getRandom(0f, maxTime)
    }

    fun updateRandom(drawable: GradientDrawable, mAlpha: Float) {
        curTime += 0.025F
        val curY = curTime * v
        if (curY - snowSize > maxY) {
            curTime = 0F
        }
        val left = (x - snowSize / 2F).roundToInt()
        val right = (x + snowSize / 2F).roundToInt()
        val top = (curY - snowSize).roundToInt()
        val bottom = curY.roundToInt()
        drawable.apply {
            setBounds(left, top, right, bottom)
            gradientRadius = snowSize / 2.2F
            alpha = (255 * mAlpha).toInt()
        }
    }

}
